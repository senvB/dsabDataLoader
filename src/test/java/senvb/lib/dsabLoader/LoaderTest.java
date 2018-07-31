/**
 *  The DSAB data loader library allows to parse information for DSAB dart leagues.
 *  Copyright (C) 2017-2018  Sven Baselau
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package senvb.lib.dsabLoader;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.TrustManagerFactory;

import senvb.lib.dsabLoader.json.LeagueDataJSON;
import senvb.lib.dsabLoader.json.RegionJSON;
import senvb.lib.dsabLoader.json.SeasonJSON;
import senvb.lib.dsabLoader.webLoader.Constants;
import senvb.lib.dsabLoader.webLoader.DataLoaderException;
import senvb.lib.dsabLoader.webLoader.LeagueDataLoader;
import senvb.lib.dsabLoader.webLoader.LeagueMetaDataLoader;
import senvb.lib.dsabLoader.webLoader.RegionLoader;
import senvb.lib.dsabLoader.webLoader.SeasonOverviewLoader;
import sun.net.www.protocol.https.DefaultHostnameVerifier;


public class LoaderTest {

    private static String USER_AGENT_HEADER_KEY = "User-Agent";

    private static String USER_AGENT_HEADER_VALUE = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:61.0) Gecko/20100101 Firefox/61.0";


    private static final int BERLIN_REGION_ID = 50;
    private static final String BERLIN_REGION_NAME = "Berlin";
    private static final int SEASON_ID = 1917;

    @Test
    public void main() throws Exception {

        String urlString = "https://www.dsab-vfs.de/VFSProject/WebObjects/VFSProject.woa/wa/rangListen?liga=5424&typ=ergebnislistePDF&saison=2656";
        URL url1 = new URL(urlString);

        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream streamProd = getClass().getClassLoader().getResourceAsStream("dsab.jks");
            if (streamProd == null) {
                System.out.println("Cannot load keystore for environment status requests");
            } else {
                keyStore.load(streamProd, "dsabdsab".toCharArray());
                streamProd.close();
            }
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
            System.out.println("Cannot set up keystore for environment status requests");
        }

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, "dsabdsab".toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), tmf.getTrustManagers(), null);
        HttpsURLConnection  conn = (HttpsURLConnection)url1.openConnection();
        conn.setSSLSocketFactory(sslContext.getSocketFactory());
        conn.setHostnameVerifier(new DefaultHostnameVerifier());
        //conn.addRequestProperty(USER_AGENT_HEADER_KEY, USER_AGENT_HEADER_VALUE);
        print_https_cert(conn);
        conn.connect();
        System.out.println(conn.getResponseCode());
        System.out.println(conn.getResponseMessage());
        System.out.println(conn.getHeaderFields());
        print_https_cert(conn);
        InputStream is = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String bla = reader.readLine();
        while(bla != null) {
            System.out.println(bla);
            bla= reader.readLine();
        }
        System.out.println("finished");
    }




    private void print_https_cert(HttpsURLConnection con){

        if(con!=null){

            try {

                System.out.println("Response Code : " + con.getResponseCode());
                System.out.println("Cipher Suite : " + con.getCipherSuite());
                System.out.println("\n");

                Certificate[] certs = con.getServerCertificates();
                for(Certificate cert : certs){
                    System.out.println("Cert Type : " + cert.getType());
                    System.out.println("Cert Hash Code : " + cert.hashCode());
                    System.out.println("Cert Public Key Algorithm : "
                            + cert.getPublicKey().getAlgorithm());
                    System.out.println("Cert Public Key Format : "
                            + cert.getPublicKey().getFormat());
                    System.out.println("\n");
                }

            } catch (SSLPeerUnverifiedException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }

        }

    }

    @Test
    public void loadRegionsTest() throws DataLoaderException, IOException {
        for (String state : Constants.getStates()) {
            List<Region> regions = RegionLoader.loadRegionsForState(state);
            System.out.println("Regions for: " + state + " (" + regions.size() + ")");
            File tempFile = File.createTempFile("dartsViewer-regions", "tmp");
            try (OutputStream os = new BufferedOutputStream(new FileOutputStream(tempFile))) {
                RegionJSON.write(os, regions);
            }
            final List<Region> r2;
            try (FileInputStream fis = new FileInputStream(tempFile)) {
                r2 = RegionJSON.read(fis);
            }
            Assert.assertEquals(regions, r2);
        }
    }

    @Test
    public void loadSeasonsTest() throws DataLoaderException, IOException {
        System.out.println("Load Seasons for " + BERLIN_REGION_NAME);
        List<Season> seasons = SeasonOverviewLoader.loadSeasons(BERLIN_REGION_ID, BERLIN_REGION_NAME);
        File tempFile = File.createTempFile("dartsViewer-seasons", "tmp");
        try (OutputStream os = new BufferedOutputStream(new FileOutputStream(tempFile))) {
            SeasonJSON.write(os, seasons);
        }
        final List<Season> s2;
        try (FileInputStream fis = new FileInputStream(tempFile)) {
            s2 = SeasonJSON.read(fis);
        }
        Assert.assertEquals(seasons, s2);
    }

    @Test
    public void loadLeaguesTest() throws DataLoaderException, IOException {
        System.out.println("Load Leagues for season ID " + SEASON_ID);
        Season season = new Season("seasonName", new Date(), new Date(), SEASON_ID, BERLIN_REGION_ID, BERLIN_REGION_NAME);
        List<LeagueMetaData> leagueMetaData = LeagueMetaDataLoader.loadLeagues(season);
        System.out.println("  number of leagues: " + leagueMetaData.size());
        for (LeagueMetaData lmd : leagueMetaData) {
            System.out.println("Load LeagueData " + lmd.getLeagueID());
            LeagueData leagueData = LeagueDataLoader.loadLeagueData(lmd, new TestProgressListener());
            String fName = "leaguesDataInSeason-" + lmd.getRegionID() + "-"
                    + lmd.getSeasonID() + "-" + lmd.getLeagueID();
            File tempFile = File.createTempFile(fName, "tmp");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                LeagueDataJSON.write(fos, leagueData);
            }
            final LeagueData leagueData2;
            try (FileInputStream fis = new FileInputStream(tempFile)) {
                leagueData2 = LeagueDataJSON.read(fis);
            }
            Assert.assertEquals(leagueData, leagueData2);
        }
    }

    private static class TestProgressListener implements LeagueDataLoader.LeagueDataLoaderProgressListener {

        @Override
        public void update(Step step) {
            System.out.println("  Loading: " + step.name());
        }
    }

}
