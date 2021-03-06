/* Copyright (c) IBM Corporation 2016. All Rights Reserved.
 * Project name: Object Generator
 * This project is licensed under the Apache License 2.0, see LICENSE.
 */

package com.ibm.og.cli;
import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;


/**
 *  class to hold command line arguments for ObjectGenerator.
 *  @since 1.0
 */

class OGGetOpt extends GetOpt {

    @Parameter(description = "<og_config>", required = true)
    private List<String> arguments = new ArrayList<String>();

    public OGGetOpt() {

    }

    public String getOGConfigFileName() {
        checkNotNull(arguments);
        return arguments.get(0);

    }

    @Override
    public boolean validate() {
        if (help || version) {
            // if command line contains help or version option, give priority to them
            return true;
        }
        checkNotNull(arguments);
        checkArgument(arguments.size() == 1, "Invalid command line arguments. Only og_config file is expected");
        return true;
    }


}
