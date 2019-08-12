package configserver.configserver;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import configserver.configserver.util.OkHttpUtil;


@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
@RestController
@RefreshScope
public class ConfigServerApplication {
	
	@RequestMapping("hello")
	public Object home(){
		return "this is config server !";
	}
	
	 @RequestMapping("/recive")
	 public void recive(){
		 System.out.println("into interface ....");
//		OkHttpUtil.postUseOkhttp("http://localhost:8086/bus/refresh", "{}");;
		 RestTemplate restTemplate = new RestTemplate();
		 ResponseEntity<String> forEntity = restTemplate.getForEntity("http://alipay.vaiwan.com/actuator/bus-refresh", String.class);
		 System.out.println(forEntity.getBody());
//		 OkHttpUtil.postUseOkhttp("http://alipay.vaiwan.com/actuator/bus-refresh", "{}");;
	 }
	
	 
	 
	 
	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
	}

}
