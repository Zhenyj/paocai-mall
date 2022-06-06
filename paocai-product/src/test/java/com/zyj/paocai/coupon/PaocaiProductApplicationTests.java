package com.zyj.paocai.coupon;

import com.zyj.paocai.product.PaocaiProductApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(classes = PaocaiProductApplication.class)
public class PaocaiProductApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testLog(){
        log.warn("警告日志");
        log.error("错误日志");
    }

}
