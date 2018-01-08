package hmm;

import java.util.*;

import hmm.be.run.*;
import hmm.be.learn.BaumWelchLearner;
import hmm.be.toolbox.KullbackLeiblerDistanceCalculator;
import hmm.be.toolbox.MarkovGenerator;

public class CreditCard {

    public enum Card {

        OK, FROUD;

        public ObservationDiscrete<Card> observation() {
            return new ObservationDiscrete<Card>(this);
        }
    };

    static public double mainLogic(String[] seq)
            throws java.io.IOException {
        /* Build a HMM and generate observation sequences using this HMM */

        Hmm<ObservationDiscrete<Card>> hmm = buildHmm();

        List<List<ObservationDiscrete<Card>>> sequences;
        sequences = generateSequences(hmm);

        /* Baum-Welch learning */

        BaumWelchLearner bwl = new BaumWelchLearner();

        Hmm<ObservationDiscrete<Card>> learntHmm = buildInitHmm();

        // This object measures the distance between two HMMs
        KullbackLeiblerDistanceCalculator klc =
                new KullbackLeiblerDistanceCalculator();

        // Incrementally improve the solution
        for (int i = 0; i < 10; i++) {
            System.out.println("Distance at iteration " + i + ": "
                    + klc.distance(learntHmm, hmm));
            learntHmm = bwl.iterate(learntHmm, sequences);
        }

        System.out.println("Resulting HMM:\n" + learntHmm);

        /* Computing the probability of a sequence */

        ObservationDiscrete<Card> packetOk = Card.OK.observation();
        ObservationDiscrete<Card> packetLoss = Card.FROUD.observation();

        List<ObservationDiscrete<Card>> testSequence =
                new ArrayList<ObservationDiscrete<Card>>();
        for (int i = 0; i < seq.length; i++) {
            if (seq[i].equals("OK")) {
                testSequence.add(packetOk);
            } else {
                testSequence.add(packetLoss);
            }

        }

//        testSequence.add(packetOk);
        System.out.println("Sequence probability: "
                + learntHmm.probability(testSequence));
        //(new GenericHmmDrawerDot()).write(learntHmm, "learntHmm.dot");
        return learntHmm.probability(testSequence);
    }

    /* The HMM this example is based on */
    static Hmm<ObservationDiscrete<Card>> buildHmm() {
        Hmm<ObservationDiscrete<Card>> hmm =
                new Hmm<ObservationDiscrete<Card>>(2,
                new OpdfDiscreteFactory<Card>(Card.class));

        hmm.setPi(0, 0.95);
        hmm.setPi(1, 0.05);

        hmm.setOpdf(0, new OpdfDiscrete<Card>(Card.class,
                new double[]{0.95, 0.05}));
        hmm.setOpdf(1, new OpdfDiscrete<Card>(Card.class,
                new double[]{0.20, 0.80}));

        hmm.setAij(0, 1, 0.05);
        hmm.setAij(0, 0, 0.95);
        hmm.setAij(1, 0, 0.10);
        hmm.setAij(1, 1, 0.90);

        return hmm;
    }

    /* Initial guess for the Baum-Welch algorithm */
    static Hmm<ObservationDiscrete<Card>> buildInitHmm() {
        Hmm<ObservationDiscrete<Card>> hmm =
                new Hmm<ObservationDiscrete<Card>>(2,
                new OpdfDiscreteFactory<Card>(Card.class));

        hmm.setPi(0, 0.50);
        hmm.setPi(1, 0.50);

        hmm.setOpdf(0, new OpdfDiscrete<Card>(Card.class,
                new double[]{0.8, 0.2}));
        hmm.setOpdf(1, new OpdfDiscrete<Card>(Card.class,
                new double[]{0.1, 0.9}));

        hmm.setAij(0, 1, 0.2);
        hmm.setAij(0, 0, 0.8);
        hmm.setAij(1, 0, 0.2);
        hmm.setAij(1, 1, 0.8);

        return hmm;
    }

    /* Generate several observation sequences using a HMM */
    static <O extends Observation> List<List<O>> generateSequences(Hmm<O> hmm) {
        MarkovGenerator<O> mg = new MarkovGenerator<O>(hmm);

        List<List<O>> sequences = new ArrayList<List<O>>();
        for (int i = 0; i < 200; i++) {
            sequences.add(mg.observationSequence(100));
        }

        return sequences;
    }
}
