package com.backendapp.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CmsApplication {
	/**
	* Kalau nanti sudah bisa ngirim email, is_email_vverified logic buat,
	 * dan getRefreshPasswordToken buat agar refreshPasswordToken dikirim lewat email
	**/
	public static void main(String[] args) {
		SpringApplication.run(CmsApplication.class, args);
	}

}
