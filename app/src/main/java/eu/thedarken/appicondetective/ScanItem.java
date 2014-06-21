package eu.thedarken.appicondetective;

public class ScanItem {
    private final String mPackageName;
    private int mHeight;
    private int mWidth;
    private String mName;

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
}
