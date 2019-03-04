package Model;

import java.io.Serializable;

/**
 * Created by yahia on 25/07/17.
 */

public class RequestModel  implements Serializable {

  private String req_id;
  private String req_date;
  private String req_status;
  private String medicine_name;
  private String medicine_itemCount;
  private String donator_id;
  private String donator_first_name;
  private String donator_last_name;
  private String donator_phone;
  private String donator_token;
  private String donator_country_id;
  private String donator_country_name;
  private String donator_city_id;
  private String donator_city_name;
  private String donator_area_id;
  private String donator_area_name;
  private String donator_address;
  private String donator_availableTime;
  private String donator_notes;
  private String voulnteer_id;
  private String voulnteer_first_name;
  private String voulnteer_last_name;
  private String voulnteer_phone;
  private String voulnteer_token;


    public RequestModel() {
        req_id = "";
        req_date = "";
        req_status = "";
        medicine_name = "";
        medicine_itemCount = "";
        donator_id = "";
        donator_first_name = "";
        donator_last_name = "";
        donator_phone = "";
        donator_token = "";
        donator_country_id = "";
        donator_country_name = "";
        donator_city_id = "";
        donator_city_name = "";
        donator_area_id = "";
        donator_area_name = "";
        donator_address = "";
        donator_availableTime = "";
        donator_notes = "";
        voulnteer_id = "";
        voulnteer_first_name = "";
        voulnteer_last_name = "";
        voulnteer_phone = "";
        voulnteer_token = "";
    }

    public String getReq_id() {
        return req_id;
    }

    public void setReq_id(String req_id) {
        this.req_id = req_id;
    }

    public String getReq_date() {
        return req_date;
    }

    public void setReq_date(String req_date) {
        this.req_date = req_date;
    }

    public String getReq_status() {
        return req_status;
    }

    public void setReq_status(String req_status) {
        this.req_status = req_status;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public String getMedicine_itemCount() {
        return medicine_itemCount;
    }

    public void setMedicine_itemCount(String medicine_itemCount) {
        this.medicine_itemCount = medicine_itemCount;
    }

    public String getDonator_id() {
        return donator_id;
    }

    public void setDonator_id(String donator_id) {
        this.donator_id = donator_id;
    }

    public String getDonator_first_name() {
        return donator_first_name;
    }

    public void setDonator_first_name(String donator_first_name) {
        this.donator_first_name = donator_first_name;
    }

    public String getDonator_last_name() {
        return donator_last_name;
    }

    public void setDonator_last_name(String donator_last_name) {
        this.donator_last_name = donator_last_name;
    }

    public String getDonator_phone() {
        return donator_phone;
    }

    public void setDonator_phone(String donator_phone) {
        this.donator_phone = donator_phone;
    }

    public String getDonator_token() {
        return donator_token;
    }

    public void setDonator_token(String donator_token) {
        this.donator_token = donator_token;
    }

    public String getDonator_country_id() {
        return donator_country_id;
    }

    public void setDonator_country_id(String donator_country_id) {
        this.donator_country_id = donator_country_id;
    }

    public String getDonator_country_name() {
        return donator_country_name;
    }

    public void setDonator_country_name(String donator_country_name) {
        this.donator_country_name = donator_country_name;
    }

    public String getDonator_city_id() {
        return donator_city_id;
    }

    public void setDonator_city_id(String donator_city_id) {
        this.donator_city_id = donator_city_id;
    }

    public String getDonator_city_name() {
        return donator_city_name;
    }

    public void setDonator_city_name(String donator_city_name) {
        this.donator_city_name = donator_city_name;
    }

    public String getDonator_area_id() {
        return donator_area_id;
    }

    public void setDonator_area_id(String donator_area_id) {
        this.donator_area_id = donator_area_id;
    }

    public String getDonator_area_name() {
        return donator_area_name;
    }

    public void setDonator_area_name(String donator_area_name) {
        this.donator_area_name = donator_area_name;
    }

    public String getDonator_address() {
        return donator_address;
    }

    public void setDonator_address(String donator_address) {
        this.donator_address = donator_address;
    }

    public String getDonator_availableTime() {
        return donator_availableTime;
    }

    public void setDonator_availableTime(String donator_availableTime) {
        this.donator_availableTime = donator_availableTime;
    }

    public String getDonator_notes() {
        return donator_notes;
    }

    public void setDonator_notes(String donator_notes) {
        this.donator_notes = donator_notes;
    }

    public String getVoulnteer_id() {
        return voulnteer_id;
    }

    public void setVoulnteer_id(String voulnteer_id) {
        this.voulnteer_id = voulnteer_id;
    }

    public String getVoulnteer_first_name() {
        return voulnteer_first_name;
    }

    public void setVoulnteer_first_name(String voulnteer_first_name) {
        this.voulnteer_first_name = voulnteer_first_name;
    }

    public String getVoulnteer_last_name() {
        return voulnteer_last_name;
    }

    public void setVoulnteer_last_name(String voulnteer_last_name) {
        this.voulnteer_last_name = voulnteer_last_name;
    }

    public String getVoulnteer_phone() {
        return voulnteer_phone;
    }

    public void setVoulnteer_phone(String voulnteer_phone) {
        this.voulnteer_phone = voulnteer_phone;
    }

    public String getVoulnteer_token() {
        return voulnteer_token;
    }

    public void setVoulnteer_token(String voulnteer_token) {
        this.voulnteer_token = voulnteer_token;
    }


}
