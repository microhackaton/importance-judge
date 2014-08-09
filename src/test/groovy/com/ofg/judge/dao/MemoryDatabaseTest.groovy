package com.ofg.judge.dao

import com.ofg.twitter.controller.relations.Relationship
import com.ofg.twitter.places.RelationshipExamples
import spock.lang.Specification

class MemoryDatabaseTest extends Specification {

    def "With empty database, new request should be the same"() {
        given:
            MemoryDatabase memoryDatabase = new MemoryDatabase()

        expect:
            memoryDatabase.updateRelationship(RelationshipExamples.withBarPlace()) == RelationshipExamples.withBarPlace()

        where:
            input << RelationshipExamples.withBarPlace()
    }

    def "With database with one record, new request should return merged description"() {
        given:
            MemoryDatabase memoryDatabase = new MemoryDatabase()
            memoryDatabase.updateRelationship(firstRequest)

        when:
            Relationship result = memoryDatabase.updateRelationship(secondRequest)

        then:
            result == expectedResult

        where:
            firstRequest                        | secondRequest                       | expectedResult
            RelationshipExamples.withFooPlace() | RelationshipExamples.withBarPlace() | RelationshipExamples.withFooAndBarPlace()
    }
}
