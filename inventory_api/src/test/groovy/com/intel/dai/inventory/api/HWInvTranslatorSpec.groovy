// Copyright (C) 2019-2020 Intel Corporation
//
// SPDX-License-Identifier: Apache-2.0
//
package com.intel.dai.inventory.api

import com.intel.dai.dsimpl.voltdb.HWInvUtilImpl
import com.intel.dai.inventory.api.pojo.fru.ForeignFRU
import com.intel.dai.inventory.api.pojo.hist.ForeignHWInvHistoryEvent
import com.intel.dai.inventory.api.pojo.loc.ForeignHWInvByLocMemory
import com.intel.logging.Logger
import spock.lang.Specification

import java.nio.file.Paths

class HWInvTranslatorSpec extends Specification {
    static def dataDir = "src/test/resources/data/"

    HWInvTranslator ts

    def setupSpec() {
        "rm noSuchFile".execute().text
    }

    def setup() {
        ts = new HWInvTranslator()
    }

    def "Test extractParentId"() {
        expect: ts.extractParentId(id) == parentId

        where:
        id     || parentId
        "x0n0" || "x0"
        "x0"   || ""
        ""     || ""
        "123"  || ""
    }

    def "toCanonical from ForeignHWInvByLoc - negative" () {
        def arg = new ForeignHWInvByLocMemory()
        arg.ID = ID
        arg.Type = Type
        arg.Ordinal = Ordinal
        arg.Status = Status
        arg.PopulatedFRU = PopulatedFRU

        expect: ts.toCanonicalLoc(arg) == null

        where:
        ID      | Type      | Ordinal   | Status            | PopulatedFRU
        null    | "Type"    | 0         | "Empty"           | null
        "ID"    | null      | 0         | "Empty"           | null
        "ID"    | "Type"    | -1        | "Empty"           | null
        "ID"    | "Type"    | 0         | null              | null
        "ID"    | "Type"    | 0         | "Populated"       | null
        "ID"    | "Type"    | 0         | "noSuchStatus"    | null
        "ID"    | "Type"    | 0         | "Empty"           | new ForeignFRU()
    }

    def "extractParentId"() {
        expect: ts.extractParentId(candidate) == result

        where:
        candidate       | result
        "x0"            | ""
        "x0c0s0b0n0"    | "x0c0s0b0"
        "x0c0s0b0n"     | ""
        "^#^@!"         | ""
        null            | null
    }

    def "foreignToCanonical - Path"() {
        def ts = new HWInvTranslator(new HWInvUtilImpl(Mock(Logger)))
        def res = ts.foreignToCanonical(Paths.get(inputFileName))
        println "Translated " + inputFileName + ":"
        println res.getValue()
        expect:
        res.getKey() == location

        where:
        inputFileName                                           || location
        dataDir+"foreignHwByLoc/flatNode.json"                  || "x0c0s26b0n0"
        dataDir+"foreignHwByLocList/preview4HWInventory.json"   || ""
        dataDir+"foreignHwByLocList/inv_loc.json"               || ""
        dataDir+"foreignHwInventory/nodeNoMemoryNoCpu.json"     || "x0c0s21b0n0"
        dataDir+"foreignHwInventory/hsm-inv-hw-query-s0.json"   || "s0"
    }

    def "toCanonical from ForeignHWInvHistoryEvent - negative" () {
        def arg = new ForeignHWInvHistoryEvent()
        arg.ID = ID
        arg.EventType = EventType
        arg.Timestamp = Timestamp
        arg.FRUID = FRUID

        expect: ts.toCanonical(arg) == null

        where:
        ID      | EventType | Timestamp | FRUID
        null    | "Type"    | "ts"      | "FRUID"
        "ID"    | null      | "ts"      | "FRUID"
        "ID"    | "Type"    | null      | "FRUID"
        "ID"    | "Type"    | "ts"      | null
    }

    def "toCanonical from ForeignHWInvHistoryEvent" () {
        def arg = new ForeignHWInvHistoryEvent()
        arg.ID = ID
        arg.EventType = EventType
        arg.Timestamp = Timestamp
        arg.FRUID = FRUID

        expect: ts.toCanonical(arg) != null

        where:
        ID      | EventType | Timestamp | FRUID
        "ID"    | "Type"    | "ts"      | "FRUID"
    }

    def "getValue" () {
        expect: ts.getValue(Json, Name) == Value

        where:
        Json                        | Name      || Value
        '{"name": "value"}'         | 'name'    || '"value"'
        '{"name": {"n": "v"}}'      | 'name'    || '{"n":"v"}'
        '{"name": [{"n": "v"}]}'    | 'name'    || '[{"n":"v"}]'
        '{"name": null }'           | 'name'    || 'null'
        '{"name": true }'           | 'name'    || 'true'
        '{"name": false }'          | 'name'    || 'false'
    }
}
