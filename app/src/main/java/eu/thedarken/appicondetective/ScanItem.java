package eu.thedarken.appicondetective;

public class ScanItem {
    private final String mPackageName;
    private int mHeight = -1;
    private int mWidth = -1;
    private String mName;
    private String mType;

    public ScanItem(String packageName) {
        mPackageName = packageName;
    }

    public int getPxCount() {
        return getWidth() * getHeight();
    }

    public int getHeight() {
        return mHeight;
    }

    public int getWidth() {
        return mWidth;
    }

    public String getName() {
        return mName;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    @Override
    public String toString() {
        return getPxCount() + ";" + getPackageName() + ";" + getWidth() + ";" + getHeight() + ";" + getName();
    }

    public String toUserReadableString(int normalSize) {
        StringBuilder builder = new StringBuilder();
        builder.append("AppName:" + getName() + "\n");
        builder.append("PackageName:" + getPackageName() + "\n");
        builder.append("Width x Height:" + getWidth() + "x" + getHeight() + "\n");
        int percent = (int) ((getPxCount() * 100.0f) / normalSize);
        builder.append("Above default:" + percent + "%");
        return builder.toString();
    }


}
