'use strict';
const MANIFEST = 'flutter-app-manifest';
const TEMP = 'flutter-temp-cache';
const CACHE_NAME = 'flutter-app-cache';

const RESOURCES = {"assets/AssetManifest.bin": "373f87f7ab52469e5aeacc01ba9492e9",
"assets/AssetManifest.bin.json": "9f7cf5949df423fc3b96bc67e3a4c0b9",
"assets/AssetManifest.json": "b727ffe3332cc61cfb795b67182fb701",
"assets/assets/icons/androidlogo.png": "b94783b93045da99c3ec234c0fe7136d",
"assets/assets/icons/logo.png": "051b8eacf244899b2ffcf1ee64bdd30f",
"assets/assets/icons/shop_logo.png": "ea74298da6d8a2f46ad014fcf261232f",
"assets/assets/images/1.5x/app_logo_50.png": "a560df990d77720e8dc3412fe5a494c1",
"assets/assets/images/1.5x/background_guide.png": "a9fad7f31febb77a841f6e72a307cd51",
"assets/assets/images/1.5x/full_year_bg.png": "191fc3d88af5ab8120d905acc83e3369",
"assets/assets/images/1.5x/half_year_bg.png": "ae67d94d09de0b3d078b231b44364c2e",
"assets/assets/images/1.5x/product.png": "74c47addce324e7f59b9c722cef144cb",
"assets/assets/images/1.5x/shop_logo.png": "2466cffec7b05a0d3fecbd60c9cdfbd5",
"assets/assets/images/1.5x/shop_logo_big.png": "b53805ed0ba6f8532f00207d091d3b1b",
"assets/assets/images/1.5x/vietqr.png": "4c7694c08ca1c25df072278a024bcfef",
"assets/assets/images/2.0x/app_logo_50.png": "ed89825fa1576b6405f9b4751fca8de5",
"assets/assets/images/2.0x/background_guide.png": "90a3cd40b721851a39af8b60baa48c14",
"assets/assets/images/2.0x/full_year_bg.png": "6174eca7872ade43d8d51235ebc6ec1f",
"assets/assets/images/2.0x/half_year_bg.png": "7200b3420684242150f54af87f5701ec",
"assets/assets/images/2.0x/product.png": "6e3a6a856d9ac4f965744d4771fa8d9a",
"assets/assets/images/2.0x/shop_logo.png": "77d654cb251766f1622e524c73b0d798",
"assets/assets/images/2.0x/shop_logo_big.png": "2c505ac7aff0224f366608b170674104",
"assets/assets/images/2.0x/vietqr.png": "90a9db99a244d2c3756b0e873883bd73",
"assets/assets/images/3.0x/app_logo_50.png": "112ca8e5d50cbe408a2b736cacc87fc3",
"assets/assets/images/3.0x/background_guide.png": "afbbd5aa24965b65d928b99fb4294053",
"assets/assets/images/3.0x/full_year_bg.png": "f331be4650d8769ab9ccbdd64a1859f6",
"assets/assets/images/3.0x/half_year_bg.png": "1b249d7b19f3fc83febe7cb43944ec6b",
"assets/assets/images/3.0x/product.png": "91fce27d043c2f4ae54048b596f5417c",
"assets/assets/images/3.0x/shop_logo.png": "bce5dcbcb41d0c589ea3fafbe39c99de",
"assets/assets/images/3.0x/shop_logo_big.png": "b50d0beebbc4d6b6d110977c7348391d",
"assets/assets/images/3.0x/vietqr.png": "1c8d3932b661e4df274382f0031955d6",
"assets/assets/images/4.0x/app_logo_50.png": "6021976e463f17a799ba7cc67a8f6a7c",
"assets/assets/images/4.0x/background_guide.png": "775eb65c19d1c0e6b0409cb66cbe7d76",
"assets/assets/images/4.0x/full_year_bg.png": "abdb2826463eac8abb6c83417ba51d5f",
"assets/assets/images/4.0x/half_year_bg.png": "e5e7667a650a9628e0dd8e42bba77737",
"assets/assets/images/4.0x/product.png": "cdc90f1364304c82c8d61d0d7532238d",
"assets/assets/images/4.0x/shop_logo.png": "73ede785d8f8b278fc4100d315770886",
"assets/assets/images/4.0x/shop_logo_big.png": "a4ca22829ad8c83964d7ed9b1258ec61",
"assets/assets/images/4.0x/vietqr.png": "3c7db3d402e48b7fb3c7ec9c31bdec4a",
"assets/assets/images/app_logo_50.png": "6544e0e4b38d8674ced22a5e43afbd57",
"assets/assets/images/background_guide.png": "612b12adca8d326d854e078e718e6fde",
"assets/assets/images/full_year_bg.png": "9ecb4f42c7e096fada5cf02a7943bb0d",
"assets/assets/images/half_year_bg.png": "c29ae58068c03d3af37308476b18f476",
"assets/assets/images/product.png": "8d0f068ab0bd0eecf659c56cb949a4d7",
"assets/assets/images/shop_logo.png": "ea74298da6d8a2f46ad014fcf261232f",
"assets/assets/images/shop_logo_big.png": "0b28a5a00431c49eb3bb58dfc9953b9d",
"assets/assets/images/vietqr.png": "a33eb2bea044ab64c409c404fb8c63b1",
"assets/assets/svg/add_picture.svg": "ce9f39f1a2e6845ff2efda31470d9d85",
"assets/assets/svg/back.svg": "57648c19d1c0a68945a6945b80d2e44f",
"assets/assets/svg/back_big.svg": "a7a2fcefce5334552259afb25b4732b8",
"assets/assets/svg/bank_card.svg": "b15e7c207c6bdfa5e46433345a53e4bf",
"assets/assets/svg/barcode.svg": "46c2ab72de6af1079f57c18d724d05c6",
"assets/assets/svg/barcode_business_tools_information.svg": "c10650fe138ebe1227814896295c5faa",
"assets/assets/svg/barcode_small.svg": "07e75be1750e7dfe9d2dc121997f1518",
"assets/assets/svg/basket_remove.svg": "8152df436460457b1ae8c512838bff76",
"assets/assets/svg/book_store.svg": "97e145b4e5d676cd593a49c26d0cef83",
"assets/assets/svg/boss_scheme.svg": "9ab93c1c9358c9b9f16a2d6c8e1b8e33",
"assets/assets/svg/browser_website.svg": "6b1717b9ff0c092dc552302e5bc08d0f",
"assets/assets/svg/buyer.svg": "2095b58f96cab7b33282cf6ff62c7ee5",
"assets/assets/svg/buyer_group.svg": "f625bc7fee74298fe0644c7b76f120dc",
"assets/assets/svg/calendar_date.svg": "704d68f76a3acc6c72835576d9c891f7",
"assets/assets/svg/calendar_month.svg": "4cdd45b8d228f85ca8f161b73cc94069",
"assets/assets/svg/camera_add.svg": "1c1e95b22fecf22c2ff1f193b21372f3",
"assets/assets/svg/cancel_checkout.svg": "c88fc0661cd3223bb27e10bbc2c87369",
"assets/assets/svg/cart.svg": "a945a9692308bb48e5e87c9a4a491070",
"assets/assets/svg/cart_check.svg": "96f5c688624c65deee7e0c21644302cc",
"assets/assets/svg/cat_face_with_wry_smile.svg": "69dc5d1332f7ae04134803d92b52e542",
"assets/assets/svg/chart_line_up.svg": "1d419ed088f47c25c41e8f89c1db8178",
"assets/assets/svg/check.svg": "7a504b99701e2b99a5e6b831c020869c",
"assets/assets/svg/checked.svg": "aa8bdb3f20b6a16722f38f37e746ace0",
"assets/assets/svg/check_sheet.svg": "ddd5ce7c0ff5149f1736977a82b87cc4",
"assets/assets/svg/close.svg": "957051ca365bca9c0b5919daa8de3091",
"assets/assets/svg/close_circle.svg": "b51497754160ebb3b7ec909456412774",
"assets/assets/svg/close_circle_fill.svg": "2ffb3b327d3330f623b2faa54169d312",
"assets/assets/svg/close_navigator.svg": "40dbb7f59aef9fad79640fa3d05c0914",
"assets/assets/svg/coffee.svg": "d2107fca814fbf25da1a102327bf5fe0",
"assets/assets/svg/collapse.svg": "83822bfcd2533cb154ac44eea5263e20",
"assets/assets/svg/copy.svg": "2855651d092a06d761c3c3f5421ab4ac",
"assets/assets/svg/corner_top_left.svg": "3a037c538d2627dbb11ebb8d80822b1b",
"assets/assets/svg/create_order.svg": "c0cdd13f945b4739e3688529f0c5ddd1",
"assets/assets/svg/currency_revenue.svg": "4cf12801a708f98b473f4f221a36cb5f",
"assets/assets/svg/currency_revenue_solid.svg": "61e9b5a553bc989df97c5dcdbb776ec7",
"assets/assets/svg/currency_value.svg": "c85a69196fcf3723f3821efc3ddd9d92",
"assets/assets/svg/debt_collection_round.svg": "5b9bb74966f3cc10456738e392d27c51",
"assets/assets/svg/delete.svg": "7394c800d17456a3ddb2b620a7a68a75",
"assets/assets/svg/delivery.svg": "424a60753c6293f88a4ae7ffa027adf0",
"assets/assets/svg/discount_ecommerce_price.svg": "fbab75ca0395d37574c4ab9f749b7103",
"assets/assets/svg/down_arrow.svg": "e0216f6ef53d4999b458f9d9ca28ea43",
"assets/assets/svg/down_arrow_backup_2.svg": "0d05ec2523dcc0e0c111ac85bc55b347",
"assets/assets/svg/down_chevron.svg": "a593bca7728a513669bb6157c7c5154d",
"assets/assets/svg/ecommerce_money.svg": "433b721afb35e86f50b689ed25d417af",
"assets/assets/svg/edit.svg": "fd4a1bd5238b7399bdfdc62303743698",
"assets/assets/svg/edit_3.svg": "fd295bbb9ba79ba95672347aad665d7e",
"assets/assets/svg/edit_clipboard.svg": "065c466f950c34a8a5a500df36929a50",
"assets/assets/svg/edit_pencil_line_01.svg": "50f3583918389552f78b022a9334ebfa",
"assets/assets/svg/export.svg": "3bb39f714ef773a006c106332a7a0231",
"assets/assets/svg/export_intime.svg": "fa7731f15b3d8fd217e39c1af3a3d6f3",
"assets/assets/svg/eye.svg": "2abd078281701652eb91de95a6f77a62",
"assets/assets/svg/eye_slash.svg": "a1a5e71e8bd277adcf6c55dafe91b4ae",
"assets/assets/svg/facebook.svg": "75e18206814bad58f41caa81a035cff1",
"assets/assets/svg/facebook_messenger.svg": "36c99c946431b1e9685871fb0de2c7bd",
"assets/assets/svg/filter.svg": "686574d331e70063155e49bb353d8fab",
"assets/assets/svg/flash_on.svg": "f95c8076f1935c84594f62e48f9bf8a7",
"assets/assets/svg/goods.svg": "8b90791e5d64e18f411949ce02373a56",
"assets/assets/svg/grid.svg": "e1ff19c52f2b25ea39998f42035597a2",
"assets/assets/svg/grid_horizontal.svg": "b82b13b08c7c00f50e39cc4a56eacf74",
"assets/assets/svg/grid_left_panel.svg": "3b9962bd4c2d549e94e1bf38c00b3713",
"assets/assets/svg/guide.svg": "357d591fb97d0840226f22293873ae74",
"assets/assets/svg/history.svg": "68fd3b0dd914934165b039ae7a4534b5",
"assets/assets/svg/home_bar.svg": "b98b6f3ce673dc83efa4e3258cc77056",
"assets/assets/svg/host_hardware.svg": "5d562e4ab2bc18816fd5db8d14c298eb",
"assets/assets/svg/hotel_man.svg": "18d18363e0b09adda551ec450796061c",
"assets/assets/svg/import.svg": "82d6ae52d3aaea66d00d5e47fc83b874",
"assets/assets/svg/import_arrows.svg": "52107d89ca19425aa722435fcd3293d1",
"assets/assets/svg/income.svg": "be79d8147d0a8e02d33ce4c07e549ef8",
"assets/assets/svg/income_amount.svg": "f6c3b4f0c617bb54cd1ce6bd20506035",
"assets/assets/svg/ingredients.svg": "4f52da9f24d32761ba9a4ab28be5eab3",
"assets/assets/svg/in_out_bar.svg": "18888498ed2e0e6ee1d033e7a548e4e7",
"assets/assets/svg/key_return_fill.svg": "e728ef3441165266a16643ea48ffc601",
"assets/assets/svg/label.svg": "c2224a7701449b731817aeafb7e467c5",
"assets/assets/svg/link.svg": "41d8cd36501c282cb74da697f146b9ea",
"assets/assets/svg/list_ul.svg": "cc8497d87c0c3dd00a7a9e16418019bb",
"assets/assets/svg/logo.svg": "e85999ebdb082bf80b36593ff4d071b4",
"assets/assets/svg/logo_50.svg": "46add5d01080e8839dad74b466c5aa1b",
"assets/assets/svg/menu.svg": "1968338a505fe8582ff127799cb0b45f",
"assets/assets/svg/minus.svg": "a4e2c2234ac575aaeee01edbca44b797",
"assets/assets/svg/minus_circle.svg": "e1e69d739bd31e3d87ef5d85c50cabc4",
"assets/assets/svg/money.svg": "acd413470c6924c3659d4e3007991ec1",
"assets/assets/svg/money_alt.svg": "343d96d5a0866c9b0a50e7d38899c9dd",
"assets/assets/svg/money_dollar_coin.svg": "ba2880a2bdac930d4dd370bf00060fb3",
"assets/assets/svg/more_circle.svg": "198827debbc9e1a0937aaa2968d4d812",
"assets/assets/svg/navigate_next.svg": "6986eb100ba5f9b9d8831a82cf4026cf",
"assets/assets/svg/next.svg": "428f3744497403f9decc5aeb094f6398",
"assets/assets/svg/number_sign_110.svg": "88e94d929ed462c12f87a5d84d00fde6",
"assets/assets/svg/order.svg": "a0843f8babfae1365a221ded01d35df9",
"assets/assets/svg/order_cancel.svg": "5a5fad9052d025cd1de05e32e2dfd6ed",
"assets/assets/svg/order_food.svg": "50e40bd16fd64cf83b057e78c7f04921",
"assets/assets/svg/order_high.svg": "17fd448159dbd6ee3ae84688e79634f7",
"assets/assets/svg/order_manage.svg": "08faf8ba55dfc4488df636c8300a736e",
"assets/assets/svg/order_repo.svg": "39b1280a8a7bb1c638cf2b69bfe187ab",
"assets/assets/svg/origin.svg": "ec8ea13c01e729d63c0d5bde3ee4a4e6",
"assets/assets/svg/outcome.svg": "341a9ed61095dd364dd8832b64db3b99",
"assets/assets/svg/package_check.svg": "9671c755bcf1d81849b4ad04c02b602a",
"assets/assets/svg/package_x.svg": "64dad599abdea87f235d6fdd30e9ca8c",
"assets/assets/svg/password_reset.svg": "d14659d1fb2513e80716aaef6de6c483",
"assets/assets/svg/pc_display.svg": "d8c2d23f7011269f468d122c15053ced",
"assets/assets/svg/phonebook_contacts.svg": "2de09fc6c6f6e9d8bd36b0af0db22f76",
"assets/assets/svg/phone_flip.svg": "bfb5b931d6de60f47c46fce3f3096526",
"assets/assets/svg/picture_o.svg": "8cb5df30b9db2c0aa8766c904ca340c1",
"assets/assets/svg/plus.svg": "cfb570c9f0c58b4e491fbecadd2b68e5",
"assets/assets/svg/plus_bar.svg": "f5d29d4088e9201138758504017ba3f9",
"assets/assets/svg/plus_circle.svg": "497271e21d886bafdbd3343cb37f4a90",
"assets/assets/svg/plus_circle_fill.svg": "2e7f2d56504bda2bf732a0e39c843ee6",
"assets/assets/svg/plus_large.svg": "d48ac799e12ea0d8546ce1363fbba8a1",
"assets/assets/svg/plus_large_width_2.svg": "52669e1801a238860b4b44039b1cd9ba",
"assets/assets/svg/point.svg": "7a2702603556f15057ef73dc2a5700e2",
"assets/assets/svg/points_and_dollars_exchange.svg": "811d593126aa181b8017d2a460b789f8",
"assets/assets/svg/price_tag_round.svg": "04f28ec5df0ff60c3453924d37418b58",
"assets/assets/svg/print.svg": "6c81ac8ac7628b62112de64803dcd126",
"assets/assets/svg/print_white.svg": "974e0e21220324f7db9b137278724c44",
"assets/assets/svg/product.svg": "c2716134f1fefca1530a4ae52938717a",
"assets/assets/svg/product_delivery_ecommerce.svg": "d767fb7273de9d91f1e7a2d59648d5c1",
"assets/assets/svg/product_list.svg": "335bddda786e7a1a332587630946695d",
"assets/assets/svg/product_service_campaign.svg": "3273eb28e35f0b92afab957d78e4089b",
"assets/assets/svg/profile_round.svg": "d7627383fdd488c907284b35e4c8dea7",
"assets/assets/svg/profit_graph.svg": "ef15a2178d075f437cf386e8680a4e7d",
"assets/assets/svg/qrcode.svg": "d2f22b17ae731544d4f31abe2720ebf2",
"assets/assets/svg/report.svg": "0c80e0cccf118aedb9c981bb2b56a520",
"assets/assets/svg/report_ecommerce_money.svg": "d94c08dc0818b919561c77799c228ba7",
"assets/assets/svg/report_header.svg": "46ea723df6c06dde7944eda03c0f6c81",
"assets/assets/svg/report_order.svg": "09564f91c20a7c40036ed6530ae9b034",
"assets/assets/svg/report_product.svg": "dea7872052b93ced1de099ecd4a3c62e",
"assets/assets/svg/report_warehouse.svg": "a038b8863ae783cfb0cbb19fb2485aa0",
"assets/assets/svg/sales_amount.svg": "e571d50221365daf79b21f9355903a93",
"assets/assets/svg/search.svg": "0fa88b282ebb973418c6faaa3c6b8773",
"assets/assets/svg/send.svg": "fe962baa4185371751dbe85f3e3b3934",
"assets/assets/svg/setting.svg": "24e1b97ada85e580afc5af6cfd8ac5e2",
"assets/assets/svg/setting_preferences_basic.svg": "9f250faee5778913c24fa025a5338189",
"assets/assets/svg/share.svg": "fdc8b7f49644c8d9988a06f9233ce2bf",
"assets/assets/svg/small_chart.svg": "940736e29d14473082a343cae115d83d",
"assets/assets/svg/sms.svg": "19eb9d8b4a6530faf14977ab6df00c1d",
"assets/assets/svg/sort_by.svg": "b6341c4cf26ca0004e0ea7bcd5418bdd",
"assets/assets/svg/stack.svg": "aadee0351e69a7b66f928fcf6cf89add",
"assets/assets/svg/staff.svg": "54a11fb82a3b125e649d3292e7fd2f93",
"assets/assets/svg/store.svg": "e0c0b75c2c00d55f763025381d62cd8d",
"assets/assets/svg/support.svg": "26a1b17a807b1f77941ac18fae39c95e",
"assets/assets/svg/table.svg": "07031300e35bdda455c84b90db5b3013",
"assets/assets/svg/table_filled.svg": "73ba5e8912d4f5abebba4fb41c2b4542",
"assets/assets/svg/table_order.svg": "07354eefe2f5fe8da1ed020490ab9c63",
"assets/assets/svg/time.svg": "759f3d074ec0c0baf2d187c1e3f08f3d",
"assets/assets/svg/timer.svg": "1fe9543d7b12d74421536780efde88dc",
"assets/assets/svg/time_filled.svg": "c6437b596203ce975d03763db79c2fc7",
"assets/assets/svg/up_arrow.svg": "168f969f0af0534286b6624815038774",
"assets/assets/svg/up_down.svg": "de62898488de4732da828b705a1385ac",
"assets/assets/svg/wallet.svg": "e4a8de3e7891577082681aba1b73985c",
"assets/assets/svg/warehouse.svg": "00d62ea5495fa8e4422deb20ed96580a",
"assets/assets/svg/warehouse_result.svg": "261822b5b535c1a6588f5d610547ec0c",
"assets/assets/svg/warehouse_small.svg": "f08f93062e3c47505187898135ef7459",
"assets/assets/svg/warning_circle.svg": "99bb2db0d8a540ee7e9edc9834790ddf",
"assets/assets/svg/web.svg": "29ec7e3d80a17a2e171b0bc55b1f69d4",
"assets/assets/svg/web_link.svg": "c53975dfe9c4bfe55355ac99fa7bfdfa",
"assets/FontManifest.json": "dc3d03800ccca4601324923c0b1d6d57",
"assets/fonts/MaterialIcons-Regular.otf": "a0e798f5aa88a53d5bc72c3afaa47c61",
"assets/NOTICES": "a480a5b028c0d5c87f7efba57a6c5267",
"assets/packages/cupertino_icons/assets/CupertinoIcons.ttf": "9d7fc783d76547e0a07bffd917ad3a6d",
"assets/packages/flutter_pos_printer_platform_image_3_sdt/resources/capabilities.json": "b65abddfd828b29b21a561053b4d3883",
"assets/shaders/ink_sparkle.frag": "ecc85a2e95f5e9f53123dcaf8cb9b6ce",
"canvaskit/canvaskit.js": "140ccb7d34d0a55065fbd422b843add6",
"canvaskit/canvaskit.js.symbols": "58832fbed59e00d2190aa295c4d70360",
"canvaskit/canvaskit.wasm": "07b9f5853202304d3b0749d9306573cc",
"canvaskit/chromium/canvaskit.js": "5e27aae346eee469027c80af0751d53d",
"canvaskit/chromium/canvaskit.js.symbols": "193deaca1a1424049326d4a91ad1d88d",
"canvaskit/chromium/canvaskit.wasm": "24c77e750a7fa6d474198905249ff506",
"canvaskit/skwasm.js": "1ef3ea3a0fec4569e5d531da25f34095",
"canvaskit/skwasm.js.symbols": "0088242d10d7e7d6d2649d1fe1bda7c1",
"canvaskit/skwasm.wasm": "264db41426307cfc7fa44b95a7772109",
"canvaskit/skwasm_heavy.js": "413f5b2b2d9345f37de148e2544f584f",
"canvaskit/skwasm_heavy.js.symbols": "3c01ec03b5de6d62c34e17014d1decd3",
"canvaskit/skwasm_heavy.wasm": "8034ad26ba2485dab2fd49bdd786837b",
"favicon.png": "358e351d8f55c5af4c44d42cb108f6fa",
"flutter.js": "888483df48293866f9f41d3d9274a779",
"flutter_bootstrap.js": "38fc53946b1d1070e366ef72f042f29c",
"icons/Icon-192.png": "60c831a2491d4b7d618dc5a2033447f9",
"icons/Icon-512.png": "2d9c79316a49adb98208438b1ad86c61",
"icons/Icon-maskable-192.png": "60c831a2491d4b7d618dc5a2033447f9",
"icons/Icon-maskable-512.png": "2d9c79316a49adb98208438b1ad86c61",
"index.html": "65ddb833a327d08835ffc35efed772d8",
"/": "65ddb833a327d08835ffc35efed772d8",
"main.dart.js": "a756e6d57bf137d5c54e318882f044d8",
"manifest.json": "1bc72327d12e6ddd809a94b9fbaa511f",
"version.json": "47918e0e0d7a327a871971533bab8cab"};
// The application shell files that are downloaded before a service worker can
// start.
const CORE = ["main.dart.js",
"index.html",
"flutter_bootstrap.js",
"assets/AssetManifest.bin.json",
"assets/FontManifest.json"];

// During install, the TEMP cache is populated with the application shell files.
self.addEventListener("install", (event) => {
  self.skipWaiting();
  return event.waitUntil(
    caches.open(TEMP).then((cache) => {
      return cache.addAll(
        CORE.map((value) => new Request(value, {'cache': 'reload'})));
    })
  );
});
// During activate, the cache is populated with the temp files downloaded in
// install. If this service worker is upgrading from one with a saved
// MANIFEST, then use this to retain unchanged resource files.
self.addEventListener("activate", function(event) {
  return event.waitUntil(async function() {
    try {
      var contentCache = await caches.open(CACHE_NAME);
      var tempCache = await caches.open(TEMP);
      var manifestCache = await caches.open(MANIFEST);
      var manifest = await manifestCache.match('manifest');
      // When there is no prior manifest, clear the entire cache.
      if (!manifest) {
        await caches.delete(CACHE_NAME);
        contentCache = await caches.open(CACHE_NAME);
        for (var request of await tempCache.keys()) {
          var response = await tempCache.match(request);
          await contentCache.put(request, response);
        }
        await caches.delete(TEMP);
        // Save the manifest to make future upgrades efficient.
        await manifestCache.put('manifest', new Response(JSON.stringify(RESOURCES)));
        // Claim client to enable caching on first launch
        self.clients.claim();
        return;
      }
      var oldManifest = await manifest.json();
      var origin = self.location.origin;
      for (var request of await contentCache.keys()) {
        var key = request.url.substring(origin.length + 1);
        if (key == "") {
          key = "/";
        }
        // If a resource from the old manifest is not in the new cache, or if
        // the MD5 sum has changed, delete it. Otherwise the resource is left
        // in the cache and can be reused by the new service worker.
        if (!RESOURCES[key] || RESOURCES[key] != oldManifest[key]) {
          await contentCache.delete(request);
        }
      }
      // Populate the cache with the app shell TEMP files, potentially overwriting
      // cache files preserved above.
      for (var request of await tempCache.keys()) {
        var response = await tempCache.match(request);
        await contentCache.put(request, response);
      }
      await caches.delete(TEMP);
      // Save the manifest to make future upgrades efficient.
      await manifestCache.put('manifest', new Response(JSON.stringify(RESOURCES)));
      // Claim client to enable caching on first launch
      self.clients.claim();
      return;
    } catch (err) {
      // On an unhandled exception the state of the cache cannot be guaranteed.
      console.error('Failed to upgrade service worker: ' + err);
      await caches.delete(CACHE_NAME);
      await caches.delete(TEMP);
      await caches.delete(MANIFEST);
    }
  }());
});
// The fetch handler redirects requests for RESOURCE files to the service
// worker cache.
self.addEventListener("fetch", (event) => {
  if (event.request.method !== 'GET') {
    return;
  }
  var origin = self.location.origin;
  var key = event.request.url.substring(origin.length + 1);
  // Redirect URLs to the index.html
  if (key.indexOf('?v=') != -1) {
    key = key.split('?v=')[0];
  }
  if (event.request.url == origin || event.request.url.startsWith(origin + '/#') || key == '') {
    key = '/';
  }
  // If the URL is not the RESOURCE list then return to signal that the
  // browser should take over.
  if (!RESOURCES[key]) {
    return;
  }
  // If the URL is the index.html, perform an online-first request.
  if (key == '/') {
    return onlineFirst(event);
  }
  event.respondWith(caches.open(CACHE_NAME)
    .then((cache) =>  {
      return cache.match(event.request).then((response) => {
        // Either respond with the cached resource, or perform a fetch and
        // lazily populate the cache only if the resource was successfully fetched.
        return response || fetch(event.request).then((response) => {
          if (response && Boolean(response.ok)) {
            cache.put(event.request, response.clone());
          }
          return response;
        });
      })
    })
  );
});
self.addEventListener('message', (event) => {
  // SkipWaiting can be used to immediately activate a waiting service worker.
  // This will also require a page refresh triggered by the main worker.
  if (event.data === 'skipWaiting') {
    self.skipWaiting();
    return;
  }
  if (event.data === 'downloadOffline') {
    downloadOffline();
    return;
  }
});
// Download offline will check the RESOURCES for all files not in the cache
// and populate them.
async function downloadOffline() {
  var resources = [];
  var contentCache = await caches.open(CACHE_NAME);
  var currentContent = {};
  for (var request of await contentCache.keys()) {
    var key = request.url.substring(origin.length + 1);
    if (key == "") {
      key = "/";
    }
    currentContent[key] = true;
  }
  for (var resourceKey of Object.keys(RESOURCES)) {
    if (!currentContent[resourceKey]) {
      resources.push(resourceKey);
    }
  }
  return contentCache.addAll(resources);
}
// Attempt to download the resource online before falling back to
// the offline cache.
function onlineFirst(event) {
  return event.respondWith(
    fetch(event.request).then((response) => {
      return caches.open(CACHE_NAME).then((cache) => {
        cache.put(event.request, response.clone());
        return response;
      });
    }).catch((error) => {
      return caches.open(CACHE_NAME).then((cache) => {
        return cache.match(event.request).then((response) => {
          if (response != null) {
            return response;
          }
          throw error;
        });
      });
    })
  );
}
