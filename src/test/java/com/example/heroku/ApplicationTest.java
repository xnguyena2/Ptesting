package com.example.heroku;

import com.example.heroku.model.repository.ImageRepository;
import com.example.heroku.photo.FlickrLib;
import com.example.heroku.services.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class ApplicationTest extends TestConfig{

    String[] listImg = new String[]{
            //"C:\\Users\\phong\\Downloads\\phong\\944-900x500.jpg",
            //"C:\\Users\\phong\\Downloads\\phong\\944-900x500.jpg",
            //"C:\\Users\\phong\\Downloads\\phong\\944-900x500.jpg",
            "C:\\Users\\phong\\Downloads\\phong\\944-900x500.jpg",
            "C:\\Users\\phong\\Downloads\\phong\\984-900x500.jpg",
            "C:\\Users\\phong\\Downloads\\phong\\1011-900x500.jpg",
            "C:\\Users\\phong\\Downloads\\phong\\984-900x500.jpg"
    };


    @Autowired
    ImageRepository imageRepository;

    @Autowired
    com.example.heroku.services.Beer beerAPI;

    @Autowired
    com.example.heroku.services.Image imageAPI;

    @Autowired
    DeviceConfig deviceConfig;

    @Autowired
    ClientDevice clientDevice;

    @Autowired
    UserDevice userDeviceAPI;

    @Autowired
    UserPackage userPackageAPI;

    @Autowired
    ShippingProvider shippingProviderAPI;

    @Autowired
    UserAddress userAddressAPI;

    @Autowired
    Voucher voucherAPI;

    @Autowired
    com.example.heroku.services.ClientDevice clientDeviceAPI;

    @Test
    public void BeerTest() {
        BeerTest.builder().beerAPI(beerAPI).build().saveBeerTest();
    }

    @Test
    public void ImageTest() {
        ImageTest.builder().imageAPI(imageAPI).imageRepository(imageRepository).build().Run(listImg);
        DeviceConfigTest.builder().deviceConfig(this.deviceConfig).clientDevice(this.clientDevice).build().DeviceConfigTest();
    }


    @Autowired
    FlickrLib flickrLib;
    @Test
    public void FlickrToken(){

    }

    @Test
    public void DeviceConfigTest() {
    }

    @Test
    public void testCurrentUser() {

    }

    @Test
    public void testParsingSubmitData() throws JsonProcessingException {
        ObjectPraserTest.builder().build().BeerSubmitDataTest();
    }

    @Test
    public void testParsingBeerPackage() throws JsonProcessingException {
        ObjectPraserTest.builder().build().BeerPackageTest();
    }

    @Test
    public void testUserDevice() {
        UserDeviceTest.builder().userDeviceAPI(this.userDeviceAPI).build().UserTest();
    }

    @Test
    public void testUserAddBeerToPackage() {
        UserPackageTest.builder().userPackageAPI(userPackageAPI).build().TestUserPackage();
    }

    @Test
    public void testSaveVietNamAddress() throws IOException {
        SaveVietNamAddressTest.builder().build().SaveToDataBaseTest();
    }

    @Test
    public void testParsingShippingData() throws JsonProcessingException {
        ObjectPraserTest.builder().build().TestParsingGHN();
    }

    @Test
    public void testShippingProvider() throws Exception {
        ShippingProviderTest.builder().shippingProvider(shippingProviderAPI).build().ShipTest();
    }

    @Test
    public void testUserAddress(){
        UserAddressTest.builder().userAddressAPI(userAddressAPI).build().UserAddressTest();
    }

    @Test
    public void testVoucher(){
        VoucherTest.builder().voucherAPI(voucherAPI).build().VoucherTest();
    }

    @Test
    public void testUploadImageToGoogle() throws IOException, GeneralSecurityException {
        //PhotoLib.getInstance().RefreshToken();
    }

    @Test
    public void testBootStrapData() {

        BeerTest.builder().beerAPI(beerAPI).build().saveBeerTest();

        DeviceConfigTest.builder().deviceConfig(this.deviceConfig).clientDevice(this.clientDevice).build().DeviceConfigTestWithoutImage();

        ClientDeviceTest.builder().clientDeviceAPI(clientDeviceAPI).build().BootStrapDataWithoutImage();

        ImageTest.builder().imageAPI(imageAPI).imageRepository(imageRepository).build().Run(listImg);

        DeviceConfigTest.builder().deviceConfig(this.deviceConfig).clientDevice(this.clientDevice).build().DeviceConfigTest();

        ClientDeviceTest.builder().clientDeviceAPI(clientDeviceAPI).build().BootStrapData();
    }

    /*
    @Test
    public void testGenerateBeer() throws InterruptedException {
        ImageTest.builder().imageAPI(imageAPI).imageRepository(imageRepository).build().ImageTest(listImg);
        BeerTest.builder().beerAPI(beerAPI).build().saveBeerTest();
        BeerTest.builder().beerAPI(beerAPI).build().createPeerTest();
        ClientDeviceTest.builder().clientDeviceAPI(clientDeviceAPI).build().BootStrapDataLarge();
    }
    @Test
    public void testGenerateAuthenticationTokenFlickr() {
        //FlickrLib.getInstance();
        ImageTest.builder().imageAPI(imageAPI).imageRepository(imageRepository).build().ImageTest(listImg);
    }

*/
}
