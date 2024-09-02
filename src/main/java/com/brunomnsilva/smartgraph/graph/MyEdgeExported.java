package com.brunomnsilva.smartgraph.graph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MyEdgeExported {
    String element, inbound, outbound;

    @JsonCreator
    public MyEdgeExported(@JsonProperty("element") String element, @JsonProperty("inbound") String inbound, @JsonProperty("outbound") String outbound) {
        this.element = element;
        this.inbound = inbound;
        this.outbound = outbound;
    }

    public String getOutbound() {
        return outbound;
    }

    public String getInbound() {
        return inbound;
    }

    public String getElement() {
        return element;
    }

    public void setOutbound(String outbound) {
        this.outbound = outbound;
    }

    public void setInbound(String inbound) {
        this.inbound = inbound;
    }

    public void setElement(String element) {
        this.element = element;
    }

}
