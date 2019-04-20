package com.org.navigator;

public class Item {
    private int drawable_id;
    private String drawable_lable;

    public Item(String drawable_lable, int drawable_id) {
        this.drawable_id = drawable_id;
        // the total number
        this.drawable_lable = drawable_lable;
    }
    public int getDrawable_id() {
        return drawable_id;
    }
    /**
     * Static function, an instance
     * @return new instance
     */

    public String getDrawable_lable() {
        return drawable_lable;
    }

    public void setDrawable_lable(String drawable_lable) {
        this.drawable_lable = drawable_lable;
    }
    /**
     * Static function, an instance
     * @return new instance
     */

    public void setDrawable_id(int drawable_id) {
        this.drawable_id = drawable_id;
    }
}
