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
    StatisticServices statisticServices;

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
    UsersInfo usersInfo;

    @Autowired
    GroupImport groupImport;

    @Autowired
    UserFCMS fcmServices;

    @Autowired
    Store storeServices;

    @Autowired
    Tokens tokensServices;

    @Autowired
    DeleteRequest deleteRequest;

    @Autowired
    PaymentTransation paymentTransation;

    @Value("${account.admin.username}")
    private String adminName;

    final String mainGroup = Config.mainGroup;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    Area area;

    @Test
    public void areaAndTableTest1() {
        AreaAndTabletTest.builder().area(area).userPackageAPI(userPackageAPI).buyer(buyer).build().Test(mainGroup);
    }

    @Test
    public void areaAndTableTest2() {
        AreaAndTabletTest.builder().area(area).userPackageAPI(userPackageAPI).buyer(buyer).build().Test(anotherGroup);
    }

    @Test
    @Order(1)
    public  void storeTest() {
        StoreTest.builder().storeServices(storeServices).group(mainGroup).build().test();
    }

    @Test
    @Order(2)
    public  void tokenTest() {
        TokenTest.builder().tokens(tokensServices).group(mainGroup).build().test();
    }

    public  void deleteRequestTest() {
        DeleteRequestTest.builder().deleteRequest(deleteRequest).build().test();
    }

    @Test
    public  void fcmTest(){
        UserFCMTest.builder().userFCMAPI(fcmServices).group(mainGroup).build().UserFCMTest();
    }

    @Test
    public void userTest() {
        UserAccountTest.builder().userAccount(userAccount).usersInfo(usersInfo).adminName(adminName).passwordEncoder(passwordEncoder).group(mainGroup).build().test();
    }

    @Test
    @Order(3)
    public void BeerTest() {
//        BeerTest.builder().beerAPI(beerAPI).group(mainGroup).build().saveBeerTest();
    }

    @Test
    public void ImageTest() {
        ImageTest.builder().imageAPI(imageAPI).imageRepository(imageRepository).group(mainGroup).build().Run(listImg);
        DeviceConfigTest.builder().deviceConfig(this.deviceConfig).clientDevice(this.clientDevice).group(mainGroup).build().DeviceConfigTest();
    }

    @Test
    public void ProductImport(){
//        ProductImportTest.builder().beerAPI(beerAPI).groupImport(groupImport).group(mainGroup).build().Test();
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
        BeerTest.builder().beerAPI(beerAPI).group(mainGroup).build().saveBeerTest();
        UserPackageTest.builder().userPackageAPI(userPackageAPI).beerAPI(beerAPI).group(mainGroup).buyer(buyer).statisticServices(statisticServices).build().TestUserPackage();
        ProductImportTest.builder().beerAPI(beerAPI).groupImport(groupImport).group(mainGroup).build().Test();
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

        DeviceConfigTest.builder().deviceConfig(this.deviceConfig).clientDevice(this.clientDevice).group(mainGroup).testWithMainGroup(true).build().DeviceConfigTestWithoutImage();

        ClientDeviceTest.builder().clientDeviceAPI(clientDeviceAPI).group(mainGroup).testWithMainGroup(true).build().BootStrapDataWithoutImage();
        ClientDeviceTest.builder().clientDeviceAPI(clientDeviceAPI).group(mainGroup).build().BootStrapDataWithoutImageNew();

        ImageTest.builder().imageAPI(imageAPI).imageRepository(imageRepository).group(mainGroup).build().Run(listImg);

        DeviceConfigTest.builder().deviceConfig(this.deviceConfig).clientDevice(this.clientDevice).group(mainGroup).testWithMainGroup(true).build().DeviceConfigTest();

        ClientDeviceTest.builder().clientDeviceAPI(clientDeviceAPI).group(mainGroup).testWithMainGroup(true).build().BootStrapData();
        ClientDeviceTest.builder().clientDeviceAPI(clientDeviceAPI).group(mainGroup).testWithMainGroup(true).build().BootStrapDataNew();
    }

    @Test
    public void testPaymentTransaction(){
        PaymentTransactionTest.builder().paymentTransation(paymentTransation).statisticServices(statisticServices).group(mainGroup).build().Test();
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
    @Order(2)
    public  void tokenTest2() {
        TokenTest.builder().tokens(tokensServices).group(anotherGroup).build().test();
    }

    @Test
    public  void fcmTest2(){
        UserFCMTest.builder().userFCMAPI(fcmServices).group(anotherGroup).build().UserFCMTest();
    }

    @Test
    public void userTest2() {
        //UNIQUE id of user
        UserAccountTest.builder().userAccount(userAccount).usersInfo(usersInfo).adminName(adminName).passwordEncoder(passwordEncoder).group(anotherGroup).build().test2();
    }

    @Test
    @Order(6)
    public void BeerTest2() {
        BeerTest.builder().beerAPI(beerAPI).group(anotherGroup).build().saveBeerTest();
        UserPackageTest.builder().userPackageAPI(userPackageAPI).beerAPI(beerAPI).group(anotherGroup).buyer(buyer).statisticServices(statisticServices).build().TestUserPackage();
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
        ProductImportTest.builder().beerAPI(beerAPI).groupImport(groupImport).group(anotherGroup).build().Test();
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
        ClientDeviceTest.builder().clientDeviceAPI(clientDeviceAPI).group(anotherGroup).build().BootStrapDataWithoutImageNew();

        ImageTest.builder().imageAPI(imageAPI).imageRepository(imageRepository).group(anotherGroup).build().Run(listImg);

        DeviceConfigTest.builder().deviceConfig(this.deviceConfig).clientDevice(this.clientDevice).group(anotherGroup).build().DeviceConfigTest();

        ClientDeviceTest.builder().clientDeviceAPI(clientDeviceAPI).group(anotherGroup).build().BootStrapData();
        ClientDeviceTest.builder().clientDeviceAPI(clientDeviceAPI).group(anotherGroup).build().BootStrapDataNew();
    }

    @Test
    public void testPaymentTransaction2(){
        PaymentTransactionTest.builder().paymentTransation(paymentTransation).statisticServices(statisticServices).group(anotherGroup).build().Test();
    }

}
