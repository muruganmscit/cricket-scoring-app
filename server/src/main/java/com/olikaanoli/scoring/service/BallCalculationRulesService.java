package com.olikaanoli.scoring.service;

import com.olikaanoli.scoring.config.Extras;
import com.olikaanoli.scoring.config.KieConfig;
import com.olikaanoli.scoring.config.MapperConfig;
import com.olikaanoli.scoring.input.balls.BallInput;
import com.olikaanoli.scoring.model.Ball;
import ma.glasnost.orika.MapperFacade;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BallCalculationRulesService {

    private final KieContainer kieContainer;
    private final MapperFacade mapperFacade;

    @Autowired
    public BallCalculationRulesService(KieContainer kieContainer, MapperFacade mapperFacade) {
        this.kieContainer = kieContainer;
        this.mapperFacade = mapperFacade;
    }

    /**
     * Method to run the scoring logic using drools
     * @param ballInput Object
     * @return Ball complete Ball details to be inserted
     */
    private Ball calculateBall(BallInput ballInput) {

        /// Converting the ball input to Ball object
        Ball locBall = new Ball();
        mapperFacade.map(ballInput, locBall);

        KieSession kieSession = kieContainer.newKieSession("rulesSession");
        kieSession.insert(locBall);
        kieSession.fireAllRules();
        kieSession.dispose();
        return locBall;
    }

    public static void main(String[] args) {
        KieConfig kieConfig = new KieConfig();
        MapperConfig mapperConfig = new MapperConfig();

        BallCalculationRulesService ballCalculationRulesService =
                new BallCalculationRulesService(kieConfig.kieContainer(), mapperConfig.mapperFacade());
        /*BallInput ballInput = new BallInput();
        ballInput.setInnings(1);
        ballInput.setMatchId(1);
        ballInput.setTeamId(1);
        ballInput.setOvers(0);
        ballInput.setBall(1);
        ballInput.setBatsman(1);
        ballInput.setNonStriker(2);
        ballInput.setBowler(3);
        ballInput.setRunsTotal(0);
        Ball _2 = ballCalculationRulesService.calculateBall(ballInput);
        System.out.println(_2.getBatsmanRuns());*/

        /*BallInput ballInput1 = new BallInput();
        ballInput1.setInnings(1);
        ballInput1.setMatchId(1);
        ballInput1.setTeamId(1);
        ballInput1.setOvers(0);
        ballInput1.setBall(1);
        ballInput1.setBatsman(1);
        ballInput1.setNonStriker(2);
        ballInput1.setBowler(3);
        ballInput1.setRunsTotal(1);
        Ball _1 = ballCalculationRulesService.calculateBall(ballInput1);
        System.out.println(_1.getBatsmanRuns());*/

        BallInput ballInput1 = new BallInput();
        ballInput1.setInnings(1);
        ballInput1.setMatchId(1);
        ballInput1.setTeamId(1);
        ballInput1.setOvers(0);
        ballInput1.setBall(1);
        ballInput1.setBatsman(1);
        ballInput1.setNonStriker(2);
        ballInput1.setBowler(3);
        ballInput1.setExtraType(Extras.LEG_BYES);
        ballInput1.setRunsTotal(4);
        Ball _1 = ballCalculationRulesService.calculateBall(ballInput1);
        System.out.println(_1.getBatsmanRuns());
        System.out.println(_1.getBowlerRuns());
        System.out.println(_1.getBatsmanBall());
        System.out.println(_1.getBowlerBall());
        System.out.println(_1.getExtraRun());
    }
}
