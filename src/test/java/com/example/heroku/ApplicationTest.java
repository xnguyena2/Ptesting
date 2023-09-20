package com.example.heroku;

import com.example.heroku.model.repository.ImageRepository;
import com.example.heroku.photo.FlickrLib;
import com.example.heroku.services.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.security.GeneralSecurityException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApplicationTest extends TestConfig {

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
    com.example.heroku.services.Buyer buyer;

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
    com.example.heroku.services.ClientDevice clientDeviceAPI;

    @Autowired
    com.example.heroku.services.UserAccount userAccount;

    @Autowired
    ProductImport productImport;

    @Autowired
    UserFCMS fcmServices;

    @Autowired
    Store storeServices;

    @Value("${account.admin.username}")
    private String adminName;

    final String mainGroup = Config.mainGroup;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @Order(1)
    public  void storeTest() {
        StoreTest.builder().storeServices(storeServices).group(mainGroup).build().test();
    }

    @Test
    public  void fcmTest(){
        UserFCMTest.builder().userFCMAPI(fcmServices).group(mainGroup).build().UserFCMTest();
    }

    @Test
    public void userTest() {
        UserAccountTest.builder().userAccount(userAccount).adminName(adminName).passwordEncoder(passwordEncoder).group(mainGroup).build().test();
    }

    @Test
    @Order(3)
    public void BeerTest() {
        BeerTest.builder().beerAPI(beerAPI).group(mainGroup).build().saveBeerTest();
    }

    @Test
    public void ImageTest() {
        ImageTest.builder().imageAPI(imageAPI).imageRepository(imageRepository).group(mainGroup).build().Run(listImg);
        DeviceConfigTest.builder().deviceConfig(this.deviceConfig).clientDevice(this.clientDevice).group(mainGroup).build().DeviceConfigTest();
    }

    @Test
    public void ProductImport(){
        ProductImportTest.builder().productImport(productImport).group(mainGroup).build().Test();
    }

    @Test
    public void testParsingSubmitData() throws JsonProcessingException {
        ObjectPraserTest.builder().group(mainGroup).build().BeerSubmitDataTest();
    }

    @Test
    public void testParsingBeerPackage() throws JsonProcessingException {
        ObjectPraserTest.builder().group(mainGroup).build().BeerPackageTest();
    }

    @Test
    public void testUserDevice() {
        UserDeviceTest.builder().userDeviceAPI(this.userDeviceAPI).group(mainGroup).build().UserTest();
    }

    @Test
    @Order(4)
    public void testUserAddBeerToPackage() {
        UserPackageTest.builder().userPackageAPI(userPackageAPI).beerAPI(beerAPI).group(mainGroup).buyer(buyer).build().TestUserPackage();
    }

    @Test
    public void testSaveVietNamAddress() throws IOException {
        SaveVietNamAddressTest.builder().group(mainGroup).build().SaveToDataBaseTest();
    }

    @Test
    public void testParsingShippingData() throws JsonProcessingException {
        ObjectPraserTest.builder().group(mainGroup).build().TestParsingGHN();
    }

    @Test
    public void testShippingProvider() throws Exception {
        ShippingProviderTest.builder().shippingProvider(shippingProviderAPI).group(mainGroup).build().ShipTest();
    }

    @Test
    public void testUserAddress(){
        UserAddressTest.builder().userAddressAPI(userAddressAPI).group(mainGroup).build().UserAddressTest();
    }

    @Test
    @Order(5)
    public void testBootStrapData() {

        BeerTest.builder().beerAPI(beerAPI).group(mainGroup).build().saveBeerTest();

        DeviceConfigTest.builder().deviceConfig(this.deviceConfig).clientDevice(this.clientDevice).group(mainGroup).build().DeviceConfigTestWithoutImage();

        ClientDeviceTest.builder().clientDeviceAPI(clientDeviceAPI).group(mainGroup).build().BootStrapDataWithoutImage();

        ImageTest.builder().imageAPI(imageAPI).imageRepository(imageRepository).group(mainGroup).build().Run(listImg);

        DeviceConfigTest.builder().deviceConfig(this.deviceConfig).clientDevice(this.clientDevice).group(mainGroup).build().DeviceConfigTest();

        ClientDeviceTest.builder().clientDeviceAPI(clientDeviceAPI).group(mainGroup).build().BootStrapData();
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

    //other group testting

    final String anotherGroup = Config.otherGroup;

    @Test
    @Order(2)
    public  void storeTest2() {
        StoreTest.builder().storeServices(storeServices).group(anotherGroup).build().test();
    }

    @Test
    public  void fcmTest2(){
        UserFCMTest.builder().userFCMAPI(fcmServices).group(anotherGroup).build().UserFCMTest();
    }

    @Test
    public void userTest2() {
        //UNIQUE id of user
        UserAccountTest.builder().userAccount(userAccount).adminName(adminName).passwordEncoder(passwordEncoder).group(anotherGroup).build().test2();
    }

    @Test
    @Order(6)
    public void BeerTest2() {
        BeerTest.builder().beerAPI(beerAPI).group(anotherGroup).build().saveBeerTest();
        UserPackageTest.builder().userPackageAPI(userPackageAPI).beerAPI(beerAPI).group(anotherGroup).buyer(buyer).build().TestUserPackage();
    }

    @Test
    @Order(7)
    public void zztestUserAddBeerToPackage2() {
//        UserPackageTest.builder().userPackageAPI(userPackageAPI).beerAPI(beerAPI).group(anotherGroup).build().TestUserPackage();
    }

    @Test
    public void ImageTest2() {
        ImageTest.builder().imageAPI(imageAPI).imageRepository(imageRepository).group(anotherGroup).build().Run(listImg);
        DeviceConfigTest.builder().deviceConfig(this.deviceConfig).clientDevice(this.clientDevice).group(anotherGroup).build().DeviceConfigTest();
    }

    @Test
    public void ProductImport2(){
        ProductImportTest.builder().productImport(productImport).group(anotherGroup).build().Test();
    }

    @Test
    public void testParsingSubmitData2() throws JsonProcessingException {
        ObjectPraserTest.builder().group(anotherGroup).build().BeerSubmitDataTest();
    }

    @Test
    public void testParsingBeerPackage2() throws JsonProcessingException {
        ObjectPraserTest.builder().group(anotherGroup).build().BeerPackageTest();
    }

    @Test
    public void testUserDevice2() {
        UserDeviceTest.builder().userDeviceAPI(this.userDeviceAPI).group(anotherGroup).build().UserTest();
    }

    @Test
    public void testSaveVietNamAddress2() throws IOException {
        SaveVietNamAddressTest.builder().group(anotherGroup).build().SaveToDataBaseTest();
    }

    @Test
    public void testParsingShippingData2() throws JsonProcessingException {
        ObjectPraserTest.builder().group(anotherGroup).build().TestParsingGHN();
    }

    @Test
    public void testShippingProvider2() throws Exception {
        ShippingProviderTest.builder().shippingProvider(shippingProviderAPI).group(anotherGroup).build().ShipTest();
    }

    @Test
    public void testUserAddress2(){
        UserAddressTest.builder().userAddressAPI(userAddressAPI).group(anotherGroup).build().UserAddressTest();
    }

    @Test
    @Order(8)
    public void testBootStrapData2() {

        BeerTest.builder().beerAPI(beerAPI).group(anotherGroup).build().saveBeerTest();

        DeviceConfigTest.builder().deviceConfig(this.deviceConfig).clientDevice(this.clientDevice).group(anotherGroup).build().DeviceConfigTestWithoutImage();

        ClientDeviceTest.builder().clientDeviceAPI(clientDeviceAPI).group(anotherGroup).build().BootStrapDataWithoutImage();

        ImageTest.builder().imageAPI(imageAPI).imageRepository(imageRepository).group(anotherGroup).build().Run(listImg);

        DeviceConfigTest.builder().deviceConfig(this.deviceConfig).clientDevice(this.clientDevice).group(anotherGroup).build().DeviceConfigTest();

        ClientDeviceTest.builder().clientDeviceAPI(clientDeviceAPI).group(anotherGroup).build().BootStrapData();
    }

}
