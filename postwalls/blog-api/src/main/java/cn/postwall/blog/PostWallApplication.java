package cn.postwall.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author liuhanchao
 * @date 2022/12/10 13:14
 * @Description:
 */

@SpringBootApplication
@MapperScan("cn.postwall.blog.mapper")
public class PostWallApplication {
    public static void main(String[] args) {
        SpringApplication.run(PostWallApplication.class);
    }
}
