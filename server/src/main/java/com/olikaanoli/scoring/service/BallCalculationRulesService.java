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

    @Autowired
    public BallCalculationRulesService(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }

    /**
     * Method to run the scoring logic using drools
     * @param locBall Ball Object
     * @return Ball complete Ball details to be inserted
     */
    public Ball calculateBall(Ball locBall) {

        KieSession kieSession = kieContainer.newKieSession("rulesSession");
        kieSession.insert(locBall);
        kieSession.fireAllRules();
        kieSession.dispose();
        return locBall;
    }
}
