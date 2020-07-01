// Copyright (C) 2019-2020 Intel Corporation
//
// SPDX-License-Identifier: Apache-2.0
//
package com.intel.dai.dsapi;

import lombok.ToString;

@ToString
public class HWInvLoc {
    public String ID;
    public String Type;
    public int Ordinal;
    public String Info;

    public String FRUID;
    public String FRUType;
    public String FRUSubType;
    public String FRUInfo;

    public HWInvLoc() {
        Ordinal = -1;

        ID = "";
        Type = "";
        Info = "";
        FRUID = "";
        FRUType = "";
        FRUSubType = "";
        FRUInfo = "";
    }
}
