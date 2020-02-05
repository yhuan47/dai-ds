// Copyright (C) 2019-2020 Intel Corporation
//
// SPDX-License-Identifier: Apache-2.0
//
package com.intel.dai.dsapi;

import lombok.ToString;
import java.util.ArrayList;

// Note that if locs is marked transient, GSON cannot serialize objects of this class

/**
 * Note that if locs is marked transient, GSON cannot serialize objects of this class.
 * The constructor that can be generated by lombok has a null FRUs.  This will break
 * the unit tests.  GSON seems to be okay with this probably because it will do the
 * right thing by allocation a list.
 *
 * Note that a constructor is necessary to ensure that locs is never null.  This means that
 * an empty json results in an empty loc array.
 */
@ToString
public class HWInvTree {
    public ArrayList<HWInvLoc> locs;
    public HWInvTree() {
        locs = new ArrayList<>();
    }
}
