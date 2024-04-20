package com.example.springbatchplayground.model;

public class PremiumCustomer {


    private String Id;

    private String premiumID;
    private String name;
    private String subscribedPlan;

    public PremiumCustomer() {
    }

    public PremiumCustomer(String id, String premiumID, String name, String subscribedPlan) {
        Id = id;
        this.premiumID = premiumID;
        this.name = name;
        this.subscribedPlan = subscribedPlan;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getPremiumID() {
        return premiumID;
    }

    public void setPremiumID(String premiumID) {
        this.premiumID = premiumID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubscribedPlan() {
        return subscribedPlan;
    }

    public void setSubscribedPlan(String subscribedPlan) {
        this.subscribedPlan = subscribedPlan;
    }

    @Override
    public String toString() {
        return "PremiumCustomer{" +
                "Id='" + Id + '\'' +
                ", premiumID='" + premiumID + '\'' +
                ", name='" + name + '\'' +
                ", subscribedPlan='" + subscribedPlan + '\'' +
                '}';
    }
}
