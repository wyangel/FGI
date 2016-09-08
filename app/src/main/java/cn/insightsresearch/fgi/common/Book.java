package cn.insightsresearch.fgi.common;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/8/22.
 */
public class Book implements Parcelable
{
    private String bookName;
    private String author;
    private int publishDate;

    public Book()
    {
    }

    public String getBookName()
    {
        return bookName;
    }

    public void setBookName(String bookName)
    {
        this.bookName = bookName;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public int getPublishDate()
    {
        return publishDate;
    }

    public void setPublishDate(int publishDate)
    {
        this.publishDate = publishDate;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        out.writeString(bookName);
        out.writeString(author);
        out.writeInt(publishDate);
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>()
    {
        @Override
        public Book[] newArray(int size)
        {
            return new Book[size];
        }

        @Override
        public Book createFromParcel(Parcel in)
        {
            return new Book(in);
        }
    };

    public Book(Parcel in)
    {
        bookName = in.readString();
        author = in.readString();
        publishDate = in.readInt();
    }
}