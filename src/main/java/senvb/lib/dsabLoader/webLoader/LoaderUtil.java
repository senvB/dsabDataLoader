/**
 * The DSAB data loader library allows to parse information for DSAB dart leagues.
 * Copyright (C) 2017-2018  Sven Baselau
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package senvb.lib.dsabLoader.webLoader;


import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

final class LoaderUtil {

    private static String USER_AGENT_HEADER_KEY = "User-Agent";

    private static String USER_AGENT_HEADER_VALUE = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";

    static Elements loadHtmlSource(String url, String pattern) throws IOException {
        return Jsoup.connect(url).header(LoaderUtil.USER_AGENT_HEADER_KEY, LoaderUtil.USER_AGENT_HEADER_VALUE)
                .header("connection", "Keep-Alive").timeout(25000).get().select(pattern);
    }

    static String loadPdfFromSource(URL url) throws IOException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        InputStream is = getStream(url);
        PdfReader reader = new PdfReader(is);
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            //TextExtractionStrategy strategy = parser.processContent(i, new LocationTextExtractionStrategy());
            TextExtractionStrategy strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
            sb.append(strategy.getResultantText()).append("\n");
        }
        return sb.toString();
    }


    static boolean lineStartsWithANumber(String currentLine) {
        return !(currentLine == null || currentLine.isEmpty()) &&
                StringUtils.isNumeric(currentLine.split(StringUtils.SPACE)[0]);
    }

    static boolean lineStartsWithNumberValue(String currentLine, int value) {
        try {
            return Integer.parseInt(currentLine.split(StringUtils.SPACE)[0]) == value;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static InputStream getStream(URL url) throws NoSuchAlgorithmException, KeyManagementException, IOException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, null, null);
        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
        conn.setSSLSocketFactory(sslContext.getSocketFactory());
        conn.connect();
        return conn.getInputStream();
    }

}
