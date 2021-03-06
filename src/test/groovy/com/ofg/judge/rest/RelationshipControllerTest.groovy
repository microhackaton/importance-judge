package com.ofg.judge.rest

import com.google.common.eventbus.EventBus
import com.ofg.judge.CorrelationType
import com.ofg.judge.Relation
import com.ofg.judge.Relationship
import com.ofg.judge.dao.JudgeDAO
import spock.lang.Specification

class RelationshipControllerTest extends Specification {

    EventBus eventBus = Mock(EventBus)

    def "should store relationship in store"() {
        given:
        JudgeDAO daoMock = Mock(JudgeDAO)
        RelationshipController controller = new RelationshipController(daoMock,eventBus)
        RelationshipDto sampleDto = new RelationshipDto("1", "place", [new Relation(8, "Warsaw")])

        when:
        controller.storeRelationship(sampleDto)

        then:
        1 * daoMock.updateRelationship(new Relationship("1", CorrelationType.place, [new Relation(8, "Warsaw")]))
    }

    def "should throw when bad correlator type"() {
        given:
        JudgeDAO daoMock = Mock(JudgeDAO)
        RelationshipController controller = new RelationshipController(daoMock, eventBus)
        RelationshipDto sampleDto = new RelationshipDto("1", "plejs", [new Relation(8, "Warsaw")])

        when:
        controller.storeRelationship(sampleDto)

        then:
        ValidationError e = thrown(ValidationError)
        e.getMessage().contains("correlation type")
    }

    def "should throw when too low score"() {
        given:
        JudgeDAO daoMock = Mock(JudgeDAO)
        RelationshipController controller = new RelationshipController(daoMock, eventBus)
        RelationshipDto sampleDto = new RelationshipDto("1", "sentence", [new Relation(-1, "Warsaw")])

        when:
        controller.storeRelationship(sampleDto)

        then:
        ValidationError e = thrown(ValidationError)
        e.getMessage().contains("Bad score")
    }

}
