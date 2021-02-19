package com.example.heroku;

import com.example.heroku.model.Beer;
import com.example.heroku.model.BeerUnit;
import com.example.heroku.request.beer.BeerInfo;
import com.example.heroku.request.beer.BeerSubmitData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class BeerTest {

    com.example.heroku.services.Beer beerAPI;

    public void saveBeerTest() {

        beerAPI.CreateBeer(
                BeerInfo
                        .builder()
                        .beerUnit(new BeerUnit[]{
                                BeerUnit.builder().beer("123").name("thung").build(),
                                BeerUnit.builder().beer("123").name("lon").build()
                        })
                        .beer(Beer
                                .builder()
                                .category(Beer.Category.ALCOHOL)
                                .name("beer tiger")
                                .beer_second_id("123")
                                .build()
                                .AutoFill()
                        )
                        .build()
        ).blockLast();

        AtomicReference<String> beerUnit1ID = new AtomicReference<String>();
        AtomicReference<String> beerUnit2ID = new AtomicReference<String>();

        beerAPI.CreateBeer(
                BeerInfo
                        .builder()
                        .beerUnit(new BeerUnit[]{
                                BeerUnit
                                        .builder()
                                        .beer("456")
                                        .name("thung")
                                        .build(),
                                BeerUnit
                                        .builder()
                                        .beer("456")
                                        .name("lon")
                                        .build()
                        })
                        .beer(Beer
                                .builder()
                                .category(Beer.Category.ALCOHOL)
                                .detail("Đây là beer tiger có nồn độ cồn cao nên chú ý khi sử dụng:\n" +
                                        "- bia thơm ngon\n" +
                                        "- bia nhập ngoại\n" +
                                        "- bia sản xuất từ hà lan")
                                .name("beer tiger")
                                .beer_second_id("456").build()
                                .AutoFill()
                        )
                        .build()
        )
                .as(StepVerifier::create)
                .consumeNextWith(beerUnit -> {
                    if (beerUnit.getName().equals("lon")) {
                        beerUnit1ID.set(beerUnit.getBeer_unit_second_id());
                    } else {
                        beerUnit2ID.set(beerUnit.getBeer_unit_second_id());
                    }
                    assertThat(beerUnit.getBeer_unit_second_id()).isNotNull();

                })
                .consumeNextWith(beerUnit -> {
                    if (beerUnit.getName().equals("lon")) {
                        beerUnit1ID.set(beerUnit.getBeer_unit_second_id());
                    } else {
                        beerUnit2ID.set(beerUnit.getBeer_unit_second_id());
                    }
                    assertThat(beerUnit.getBeer_unit_second_id()).isNotNull();
                })
                .verifyComplete();


        beerAPI.CreateBeer(
                BeerInfo
                        .builder()
                        .beerUnit(new BeerUnit[]{
                                BeerUnit
                                        .builder()
                                        .beer_unit_second_id(beerUnit1ID.get())
                                        .beer("456")
                                        .price(10)
                                        .weight(0.3f)
                                        .discount(10)
                                        .date_expire(new Timestamp(new Date().getTime()))
                                        .name("thung")
                                        .build(),
                                BeerUnit
                                        .builder()
                                        .beer_unit_second_id(beerUnit2ID.get())
                                        .beer("456")
                                        .price(10)
                                        .weight(0.3f)
                                        .discount(10)
                                        .date_expire(new Timestamp(new Date().getTime()))
                                        .name("lon")
                                        .build()
                        })
                        .beer(Beer
                                .builder()
                                .category(Beer.Category.ALCOHOL)
                                .detail("Đây là beer tiger có nồn độ cồn cao nên chú ý khi sử dụng:\n" +
                                        "- bia thơm ngon\n" +
                                        "- bia nhập ngoại\n" +
                                        "- bia sản xuất từ hà lan")
                                .name("beer tiger")
                                .beer_second_id("456").build()
                                .AutoFill()
                        )
                        .build()
        )
                .sort(Comparator.comparing(BeerUnit::getName))
                .as(StepVerifier::create)
                .consumeNextWith(beerUnit -> {
                    assertThat(beerUnit.getBeer_unit_second_id()).isEqualTo(beerUnit2ID.get());
                })
                .consumeNextWith(beerUnit -> {
                    assertThat(beerUnit.getBeer_unit_second_id()).isEqualTo(beerUnit1ID.get());
                })
                .verifyComplete();

        this.beerAPI.GetBeerByID("123")
                .as(StepVerifier::create)
                .consumeNextWith(beerInfo -> {
                    assertThat(beerInfo.getBeer().getBeer_second_id()).isEqualTo("123");
                    assertThat(beerInfo.getBeer().getCategory()).isEqualTo(Beer.Category.ALCOHOL);
                    assertThat(beerInfo.getBeerUnit().length).isEqualTo(2);
                    Flux.just(beerInfo.getBeerUnit())
                            .sort(Comparator.comparing(BeerUnit::getName))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerUnit -> {
                                assertThat(beerUnit.getName()).isEqualTo("lon");
                            })
                            .consumeNextWith(beerUnit -> {
                                assertThat(beerUnit.getName()).isEqualTo("thung");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.SearchBeer("hà", 0, 100)
                .as(StepVerifier::create)
                .consumeNextWith(beerSubmitData -> {
                    assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer("hà", 0, 100)
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(1);
                    Flux.just(resultWithCount.GetResultAsArray())
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.SearchBeer("ha%oai", 0, 100)
                .as(StepVerifier::create)
                .consumeNextWith(beerSubmitData -> {
                    assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer("ha%oai", 0, 100)
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(1);
                    Flux.just(resultWithCount.GetResultAsArray())
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.SearchBeer("bia&ha&lan", 0, 100)
                .as(StepVerifier::create)
                .consumeNextWith(beerSubmitData -> {
                    assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer("bia&ha&lan", 0, 100)
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(1);
                    Flux.just(resultWithCount.GetResultAsArray())
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.SearchBeer("bi:*&ngo:*&nhap:*", 0, 100)
                .as(StepVerifier::create)
                .consumeNextWith(beerSubmitData -> {
                    assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer("bi:*&ngo:*&nhap:*", 0, 100)
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(1);
                    Flux.just(resultWithCount.GetResultAsArray())
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.GetAllBeer(0, 100)
                .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                .as(StepVerifier::create)
                .consumeNextWith(beerInfo -> {
                    assertThat(beerInfo.getBeerSecondID()).isEqualTo("123");
                    assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                })
                .consumeNextWith(beerInfo -> {
                    assertThat(beerInfo.getBeerSecondID()).isEqualTo("456");
                    assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                })
                .verifyComplete();

        this.beerAPI.CountGetAllBeer(0, 100)
                .as(StepVerifier::create)
                .consumeNextWith(searchResult -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(searchResult));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(searchResult.getCount()).isEqualTo(2);
                    Flux.just(searchResult.GetResultAsArray())
                            .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("123");
                                assertThat(beerSubmitData.getListUnit().length).isEqualTo(2);
                            })
                            .consumeNextWith(beerInfo -> {
                                assertThat(beerInfo.getBeerSecondID()).isEqualTo("456");
                                assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.GetAllBeerByCategory(Beer.Category.ALCOHOL, 0, 100)
                .sort(Comparator.comparing(Beer::getBeer_second_id))
                .as(StepVerifier::create)
                .consumeNextWith(beerInfo -> {
                    assertThat(beerInfo.getBeer_second_id()).isEqualTo("123");
                })
                .consumeNextWith(beerInfo -> {
                    assertThat(beerInfo.getBeer_second_id()).isEqualTo("456");
                })
                .verifyComplete();

        this.beerAPI.CountGetAllBeerByCategory(Beer.Category.ALCOHOL, 0, 100)
                .as(StepVerifier::create)
                .consumeNextWith(searchResult -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(searchResult));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(searchResult.getCount()).isEqualTo(2);
                    Flux.just(searchResult.GetResultAsArray())
                            .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("123");
                                assertThat(beerSubmitData.getListUnit().length).isEqualTo(2);
                            })
                            .consumeNextWith(beerInfo -> {
                                assertThat(beerInfo.getBeerSecondID()).isEqualTo("456");
                                assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                            })
                            .verifyComplete();
                })
                .verifyComplete();
    }

    public void createPeerTest() throws InterruptedException {

        BeerInfo template =
                BeerInfo
                        .builder()
                        .beerUnit(new BeerUnit[]{
                                BeerUnit
                                        .builder()
                                        .beer("456")
                                        .price(10)
                                        .weight(0.3f)
                                        .discount(10)
                                        .date_expire(new Timestamp(new Date().getTime()))
                                        .name("thung")
                                        .build(),
                                BeerUnit
                                        .builder()
                                        .beer("456")
                                        .price(10)
                                        .weight(0.3f)
                                        .discount(10)
                                        .date_expire(new Timestamp(new Date().getTime()))
                                        .name("lon")
                                        .build()
                        })
                        .beer(Beer
                                .builder()
                                .category(Beer.Category.ALCOHOL)
                                .detail("Laporta đang chạy đua chức Chủ tịch Barca nhiệm kỳ mới. Ông từng làm Chủ tịch CLB từ 2003 đến 2010, giai đoạn Lionel Messi ra mắt đội một và chinh phục loạt danh hiệu cao quý cùng Barca. Quan hệ giữa đội chủ sân Camp Nou và Messi xấu đi từ hè 2020, dưới thời cựu Chủ tịch Josep Bartomeu. Hợp đồng của siêu sao 33 tuổi chỉ còn bốn tháng và chưa có dấu hiệu cho thấy anh muốn gia hạn.\n" +
                                        "\n" +
                                        "\"Cậu ấy cần chiến thắng, cần các danh hiệu\", Laporta nói về yếu tố có thể níu chân Messi lại Camp Nou. \"Leo cần những chiếc cúp cùng Barca. Tôi sẽ làm mọi cách để giữ cậu ấy lại. Các đối thủ đang đưa ra lời đề nghị cho cậu ấy. Chúng tôi sẽ chứng minh tiền bạc không đảm bảo các danh hiệu. Leo khao khát danh hiệu hơn tiền. Leo mang tiền về Barca nhiều hơn những gì cậu ấy được trả. Cậu ấy tốn của đội 8% lợi nhuận, nhưng tạo ra khoảng 30%. Leo giúp Barca kiếm nhiều tiền, nhưng điều quan trọng nhất là sự đền đáp cảm xúc của cậu ấy. Điều vô giá là khi bạn hỏi một cô cậu nhóc nào đó về cầu thủ yêu thích nhất, và chúng trả lời đó là Messi\".\n" +
                                        "\n" +
                                        "Gặp nhiều rắc rối trước mùa giải, Messi vẫn đang là chân sút số một của Barca với 20 bàn mùa này. Đây là năm thứ 13 liên tiếp anh ghi từ 20 bàn trở lên. Messi cũng ghi bàn cho Barca ở Champions League 17 năm liền. Tuy nhiên, thành tích của Barca ngày càng đi xuống. Năm ngoái, họ thảm bại 2-8 trước Bayern ở Champions League và thua Real trong cuộc đua La Liga. Mùa này, Barca còn rất ít cơ hội đi tiếp tại Champions League sau khi thua PSG 1-4 trên sân nhà ở lượt đi vòng 1/8, và đang kém đỉnh bảng La Liga của Atletico chín điểm.\n" +
                                        "\n" +
                                        "Messi được tự do đàm phán từ tháng 1/2021 với bất kỳ đội bóng nào. Nhiều nguồn tin cho rằng anh sẽ chọn PSG hoặc Man City, nếu rời Barca.")
                                .name("beer tiger")
                                .beer_second_id("456").build()
                                .AutoFill()
                        )
                        .build();

        Thread[] threads = new Thread[100];

        for (int i = 0; i < threads.length; i++) {
            final int i1 = i;
            threads[i] = new Thread(() -> {
                System.out.println("Thread Running: " + (i1 + 1));
                String id = i1+"";
                template.getBeer().setId(null);
                template.getBeer().setBeer_second_id(id);
                template.getBeerUnit()[0].setBeer(id);
                template.getBeerUnit()[0].setId(null);
                template.getBeerUnit()[1].setBeer(id);
                template.getBeerUnit()[1].setId(null);
                beerAPI.CreateBeer(template).blockLast();
            });
            //Thread.sleep(5000);
            threads[i].start();
        }
        for (Thread t :
                threads) {
            t.join();
        }
    }
}