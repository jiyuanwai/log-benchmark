package com.jiyuanwai.log.benchmark;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;


/**
 * LoggingBenchmark
 *
 * @author xuning
 * @date 2020-01-14 19:29:25
 */
@Slf4j
@State(value = Scope.Thread)
public class LoggingBenchmark {


	private int i;

	public static void main(String[] args) throws RunnerException, IOException {
		int[] thread = {1, 4, 8, 16, 32, 64, 128};

		for (int i : thread) {
			System.err.println("start:" + i);
			String parent = "Benchmark";
			Files.createDirectories(Paths.get(parent));
			Options options = new OptionsBuilder()
					// 单进程
					.forks(0)
					// 8线程
					.threads(i)
					// 预热次数
					.warmupIterations(0)
					// 基准测试迭代次数
					.measurementIterations(1)
					// 测试持续时间
					.measurementTime(TimeValue.minutes(1))
					.include(LoggingBenchmark.class.getSimpleName())
//					.output(parent + "/Benchmark-log4j2-" + i + ".log")
//					.output(parent + "/Benchmark-log4j2-async-" + i + ".log")
					.output(parent + "/Benchmark-logback-" + i + ".log")
//					.output(parent + "/Benchmark-logback-async-" + i + ".log")
					.build();
			new Runner(options).run();
			System.err.println("end:" + i);
		}
	}

	@Benchmark
	@BenchmarkMode(Mode.Throughput)
	@OutputTimeUnit(TimeUnit.SECONDS)
	public void logThroughput() {
		printLog();
	}

	@Benchmark
	@BenchmarkMode({Mode.AverageTime, Mode.SampleTime, Mode.SingleShotTime})
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public void logAverageTimeSampleTime() {
		printLog();
	}

	private void printLog() {
		// 手工装箱，避免int缓存污染测试
		log.info("Simple {} Param String", new Integer(i++));
	}
}
