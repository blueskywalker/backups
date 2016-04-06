package org.blueskywalker.lucene.study;

import org.apache.lucene.util.Attribute;

/**
 * Created by kkim on 1/8/16.
 */
public interface GenderAttribute extends Attribute {

    public static enum Gender {Male, Female, Undefined};

    public void setGender(Gender gender);

    public Gender getGender();
}