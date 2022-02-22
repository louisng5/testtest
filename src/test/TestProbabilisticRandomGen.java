package test;

import main.ProbabilisticRandomGen.ProbabilisticRandomGen;
import main.ProbabilisticRandomGen.SimpleProbabilisticRandomGen;
import main.Throttler.SimpleThrottler;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestProbabilisticRandomGen {

    @Test
    public void ProbabilisticRandomGenTest() throws InterruptedException {
        SimpleProbabilisticRandomGen g = new SimpleProbabilisticRandomGen();
        List<ProbabilisticRandomGen.NumAndProbability> lst = new ArrayList<>();
        lst.add(new ProbabilisticRandomGen.NumAndProbability(1,0.4f));
        lst.add(new ProbabilisticRandomGen.NumAndProbability(2,0.5f));
        g.loadSample(lst);
        for (int i = 0; i < 100; i++) {
            System.out.println(g.nextFromSample());
        }
    }

}