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
package senvb.lib.dsabLoader.webLoader;

class TeamAddressData {

    private String district;
    private String name;
    private String phone;
    private String plz;
    private String street;
    private String venue;

    String resolveAddress() {
        return getStreet() + ", " + getPlz() + " " + getDistrict();
    }

    String getPhone() {
        if (phone != null) {
            return this.phone;
        }
        return "";
    }

    String getVenue() {
        if (venue != null) {
            return this.venue;
        }
        return "";
    }

    String getName() {
        if (name != null) {
            return this.name;
        }
        return "";
    }

    void setName(String name) {
        this.name = name;
    }

    String getPlz() {
        if (plz != null) {
            return this.plz;
        }
        return "";
    }

    void setPlz(String plz) {
        this.plz = plz;
    }

    void setVenue(String venue) {
        this.venue = venue;
    }

    void setPhone(String phone) {
        this.phone = phone;
    }

    private String getStreet() {
        if (street != null) {
            return this.street;
        }
        return "";
    }

    void setStreet(String street) {
        this.street = street;
    }

    private String getDistrict() {
        if (district != null) {
            return this.district;
        }
        return "";
    }

    void setDistrict(String district) {
        this.district = district;
    }
}
