/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mwired.grid.commons.commons.solr.custom;

/**
 *
 * @author kkim
 */
public class NewsDateInfluenceCustomScore2QParserPlugin extends NewsDateInfluenceCustomeScoreQParsePlugin {
    @Override
    int getThreadhold() {
        return 6;
    }    
}
