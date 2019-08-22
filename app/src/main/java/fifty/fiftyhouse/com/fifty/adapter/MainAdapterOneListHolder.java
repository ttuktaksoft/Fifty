package fifty.fiftyhouse.com.fifty.adapter;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;

public class MainAdapterOneListHolder implements AsymmetricItem {
  private int columnSpan;
  private int rowSpan;
  private int position;

  public MainAdapterOneListHolder() {
    this(1, 1, 0);
  }

  public MainAdapterOneListHolder(int columnSpan, int rowSpan, int position) {
    this.columnSpan = columnSpan;
    this.rowSpan = rowSpan;
    this.position = position;
  }

  public MainAdapterOneListHolder(Parcel in) {
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
  public static final Parcelable.Creator<MainAdapterOneListHolder> CREATOR = new Parcelable.Creator<MainAdapterOneListHolder>() {
    @Override public MainAdapterOneListHolder createFromParcel(@NonNull Parcel in) {
      return new MainAdapterOneListHolder(in);
    }

    @Override @NonNull public MainAdapterOneListHolder[] newArray(int size) {
      return new MainAdapterOneListHolder[size];
    }
  };
}