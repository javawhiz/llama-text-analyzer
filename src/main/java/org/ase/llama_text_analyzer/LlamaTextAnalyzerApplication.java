package org.ase.llama_text_analyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class LlamaTextAnalyzerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LlamaTextAnalyzerApplication.class, args);
	}

}
