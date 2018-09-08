package online.bogenliga.application.common.database;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class TestBE {

    private long id;
    private String name;
    private boolean active;
    private Boolean ready;
    private int number;
    private TestEnum state;


    public long getId() {
        return id;
    }


    public void setId(final long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(final String name) {
        this.name = name;
    }


    public int getNumber() {
        return number;
    }


    public void setNumber(final int number) {
        this.number = number;
    }


    public TestEnum getState() {
        return state;
    }


    public void setState(final TestEnum state) {
        this.state = state;
    }


    public boolean isActive() {
        return active;
    }


    public void setActive(final boolean active) {
        this.active = active;
    }


    public Boolean isReady() {
        return ready;
    }


    public void setReady(final Boolean ready) {
        this.ready = ready;
    }
}
