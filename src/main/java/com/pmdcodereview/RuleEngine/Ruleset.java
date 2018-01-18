package com.pmdcodereview.RuleEngine;

public class Ruleset {
    private String description;

    private String name;

    private Rule[] rule;

    private String xmlns;

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public Rule[] getRule ()
    {
        return rule;
    }

    public void setRule (Rule[] rule)
    {
        this.rule = rule;
    }

    public String getXmlns ()
    {
        return xmlns;
    }

    public void setXmlns (String xmlns)
    {
        this.xmlns = xmlns;
    }
}
