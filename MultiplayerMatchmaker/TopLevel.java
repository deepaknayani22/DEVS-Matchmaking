package Component.MultiplayerMatchmaker;

import java.awt.*;
import java.util.*;

import view.modeling.*;

public class TopLevel extends ViewableDigraph {
	protected static final int observationPeriod = 3000;
	protected static final int queueCapacity = 15;
	
	protected ArrayDeque<Player> Q1 = new ArrayDeque<Player>();
	protected ArrayDeque<Player> Q2 = new ArrayDeque<Player>();
	protected ArrayDeque<Player> Q3 = new ArrayDeque<Player>();
	
	public TopLevel() {
		this("TopLevel", observationPeriod, queueCapacity);
	}

	public TopLevel(String name, double observationPeriod, int queueCapacity) {
		super(name);
		make(observationPeriod, queueCapacity);
	}

	public void make(double observationPeriod, int queueCapacity) {
		addOutport("avgWaitTime");
		addOutport("matchedTeams");
		addOutport("quitPlayers");
		addOutport("rejectedPlayers");
		addOutport("match");

		ViewableAtomic PEG = new PlayerEntityGenerator("PEG", 30);
		ViewableAtomic MMH = new MatchmakingHandler("MMH", Q1, Q2, Q3);
		ViewableAtomic queue1 = new Queue1("Q1", queueCapacity, Q1, Q2, Q3);
		ViewableAtomic queue2 = new Queue2("Q2", queueCapacity, Q1, Q2, Q3);
		ViewableAtomic queue3 = new Queue3("Q3", queueCapacity, Q1, Q2, Q3);
		ViewableAtomic AL = new ActivityLogger("AL", observationPeriod);

		add(PEG);
		add(MMH);
		add(queue1);
		add(queue2);
		add(queue3);
		add(AL);

		initialize();

		addCoupling(PEG, "player", MMH, "in");
		addCoupling(MMH, "queue1", queue1, "in");
		addCoupling(MMH, "queue2", queue2, "in");
		addCoupling(MMH, "queue3", queue3, "in");
		addCoupling(MMH, "queue1", AL, "arrived");
		addCoupling(MMH, "queue2", AL, "arrived");
		addCoupling(MMH, "queue3", AL, "arrived");
		addCoupling(queue1, "match", AL, "processed");
		addCoupling(queue2, "match", AL, "processed");
		addCoupling(queue3, "match", AL, "processed");
		addCoupling(queue1, "quit", AL, "quit");
		addCoupling(queue2, "quit", AL, "quit");
		addCoupling(queue3, "quit", AL, "quit");
		addCoupling(queue1, "match", this, "match");
		addCoupling(queue2, "match", this, "match");
		addCoupling(queue3, "match", this, "match");
		addCoupling(AL, "avgWtTime", this, "avgWaitTime");
		addCoupling(AL, "avgWtTime", PEG, "stop");
		addCoupling(AL, "matchedTeamsCount", this, "matchedTeams");
		addCoupling(AL, "quitPlayers", this, "quitPlayers");
		addCoupling(AL, "rejectedPlayers", this, "rejectedPlayers");
	}

    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
    @Override
    public void layoutForSimView()
    {
        preferredSize = new Dimension(788, 447);
        ((ViewableComponent)withName("Q1")).setPreferredLocation(new Point(445, 28));
        ((ViewableComponent)withName("Q2")).setPreferredLocation(new Point(443, 159));
        ((ViewableComponent)withName("Q3")).setPreferredLocation(new Point(447, 293));
        ((ViewableComponent)withName("PEG")).setPreferredLocation(new Point(-4, 175));
        ((ViewableComponent)withName("MMH")).setPreferredLocation(new Point(185, 133));
        ((ViewableComponent)withName("AL")).setPreferredLocation(new Point(110, 292));
    }
}