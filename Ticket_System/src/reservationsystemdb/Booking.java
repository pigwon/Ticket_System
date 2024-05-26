package reservationsystemdb;

import java.util.List;

class Booking {
    Content content;
    String seatType;
    List<Integer> seats;
    String paymentMethod;
    String paymentNumber;

    Booking(Content content, String seatType, List<Integer> seats, String paymentMethod, String paymentNumber) {
        this.content = content;
        this.seatType = seatType;
        this.seats = seats;
        this.paymentMethod = paymentMethod;
        this.paymentNumber = paymentNumber;
    }

    @Override
    public String toString() {
        return content.name + " (" + content.type + "), 좌석 타입: " + seatType + ", 좌석 번호: " + seats + ", 결제 방법: " + paymentMethod + ", 결제 번호: " + paymentNumber;
    }
}
