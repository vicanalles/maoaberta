package com.maoaberta.vinicius.maoaberta.domain.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vinicius on 28/12/17.
 */

public class Notificacao implements Parcelable{

    private String title;
    private String body;
    private String icon;
    private String organizacaoId;
    private String anuncioId;
    private String senderId;
    private String timeStamp;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getOrganizacaoId() {
        return organizacaoId;
    }

    public void setOrganizacaoId(String organizacaoId) {
        this.organizacaoId = organizacaoId;
    }

    public String getAnuncioId() {
        return anuncioId;
    }

    public void setAnuncioId(String anuncioId) {
        this.anuncioId = anuncioId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.title);
        parcel.writeString(this.body);
        parcel.writeString(this.icon);
        parcel.writeString(this.organizacaoId);
        parcel.writeString(this.anuncioId);
        parcel.writeString(this.senderId);
        parcel.writeString(this.timeStamp);
    }

    protected Notificacao(Parcel in){
        this.title = in.readString();
        this.body = in.readString();
        this.icon = in.readString();
        this.organizacaoId = in.readString();
        this.anuncioId = in.readString();
        this.senderId = in.readString();
        this.timeStamp = in.readString();
    }

    public static final Parcelable.Creator<Notificacao> CREATOR = new Parcelable.Creator<Notificacao>() {
        @Override
        public Notificacao createFromParcel(Parcel parcel) {
            return new Notificacao(parcel);
        }

        @Override
        public Notificacao[] newArray(int i) {
            return new Notificacao[i];
        }
    };

    public String getNotificationId(){
        return organizacaoId + anuncioId;
    }
}
