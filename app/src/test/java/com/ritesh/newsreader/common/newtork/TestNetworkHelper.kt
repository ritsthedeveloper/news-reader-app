package com.ritesh.newsreader.common.newtork

import com.ritesh.newsreader.common.network.NetworkHelper

class TestNetworkHelper : NetworkHelper {
    override fun isNetworkConnected(): Boolean {
        return true
    }
}