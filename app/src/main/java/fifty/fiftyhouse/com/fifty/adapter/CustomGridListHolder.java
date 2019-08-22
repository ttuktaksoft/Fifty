package fifty.fiftyhouse.com.fifty.adapter;

import android.os.Parcel;
import android.os.Parcelable;

import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;

import androidx.annotation.NonNull;

public class CustomGridListHolder  implements AsymmetricItem {
    private int columnSpan;
    private int rowSpan;
    private int position;
    public String key;

    public CustomGridListHolder() {
        this(1, 1, 0, "");
    }

    public CustomGridListHolder(int columnSpan, int rowSpan, int position, String key) {
        this.columnSpan = columnSpan;
        this.rowSpan = rowSpan;
        this.position = position;
        this.key = key;
    }

    public CustomGridListHolder(Parcel in) {
        readFromParcel(in);
    }

    @Override public int getColumnSpan() {
        return columnSpan;
    }

    @Override public int getRowSpan() {
        return rowSpan;
    }

    public int getPosition() {
        return position;
    }

    @Override public String toString() {
        return String.format("%s: %sx%s", position, rowSpan, columnSpan);
    }

    @Override public int describeContents() {
        return 0;
    }

    private void readFromParcel(Parcel in) {
        columnSpan = in.readInt();
        rowSpan = in.readInt();
        position = in.readInt();
    }

    @Override public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(columnSpan);
        dest.writeInt(rowSpan);
        dest.writeInt(position);
    }

    /* Parcelable interface implementation */
    public static final Parcelable.Creator<CustomGridListHolder> CREATOR = new Parcelable.Creator<CustomGridListHolder>() {
        @Override public CustomGridListHolder createFromParcel(@NonNull Parcel in) {
            return new CustomGridListHolder(in);
        }

        @Override @NonNull public CustomGridListHolder[] newArray(int size) {
            return new CustomGridListHolder[size];
        }
    };
}
