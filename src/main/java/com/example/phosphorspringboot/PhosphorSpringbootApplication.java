package com.example.phosphorspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import edu.columbia.cs.psl.phosphor.runtime.MultiTainter;
import edu.columbia.cs.psl.phosphor.runtime.Taint;

@SpringBootApplication
@RestController
public class PhosphorSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhosphorSpringbootApplication.class, args);
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}

	@GetMapping("/demo")
	public String demo() {
		testExample1();
		testExample2();
		testExample3();
		return "DTA success!";
	}

	public static void testExample1() {
		// Creating object tags for primitives
		// Source
		int x;
		x = MultiTainter.taintedInt(100, "source 1");
		// Source
		int y;
		y = MultiTainter.taintedInt(100, "source 2");
		// Sink
		int z = x + y;
		Taint tz = MultiTainter.getTaint(z);
		assert (tz != null);
		System.out.println(tz.toString());
	}

	public static void testExample2() {
		// Creating more than 32 distinct tags (the advantage of object tags)
		// Array of 33 different sources
		double[] mydata = new double[33];
		for(int i = 0; i < mydata.length; i++) {
			mydata[i] = MultiTainter.taintedDouble((float) i, "Source " + i);
		}
		// Sink
		double result = 0.0;
		for (double elem : mydata) {
			result += elem;
		}
		Taint tag = MultiTainter.getTaint(result);
		assert (tag != null);
		System.out.println(tag.toString());
	}

	public static void testExample3() {
		// Creating object tags for primitives
		// Source
		int x;
		x = MultiTainter.taintedInt(100, "source 1");
		// Source
		int y;
		y = MultiTainter.taintedInt(100, "source 2");
		// Sink
		int z = x + y;
		// cast int to string
		String zs = Integer.toString(z);
		Taint tzs = MultiTainter.getTaint(zs);
		Taint tz = MultiTainter.getTaint(z);
		assert (tz != null);
		// tzs is not null but as seen below the deps are not right
		assert (tzs != null);
		System.out.println(tz.toString());
		// dependencies here are not right
		System.out.println(tzs.toString());
	}
}
