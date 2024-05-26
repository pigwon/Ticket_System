package reservationsystemdb;

import java.util.*;

public class Main {
    private static Map<String, User> users = new HashMap<>();
    private static Map<String, List<Content>> contents = new HashMap<>();
    private static Set<Integer> bookedSeats = new HashSet<>();
    private static final int TOTAL_SEATS = 100;
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;

    public static void main(String[] args) {
        loadContents();

    	System.out.println("----티켓팅 프로그램-----");
    	
        while (true) {
            System.out.println("1. 회원가입");
            System.out.println("2. 로그인");
            System.out.println("3. 예매");
            System.out.println("4. 예매 확인");
            System.out.println("5. 프로그램 종료");
            System.out.print("선택: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // clear the buffer

            switch (choice) {
                case 1:
                    signUp();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    bookContent();
                    break;
                case 4:
                    viewCart();
                    break;
                case 5:
                    System.out.println("프로그램 종료");
                    return;
                default:
                    System.out.println("잘못된 선택입니다.");
            }
        }
    }

    private static void signUp() {
        System.out.print("이름: ");
        String name = scanner.nextLine();
        System.out.print("아이디 (4자리 이상 숫자): ");
        String id = scanner.nextLine();
        System.out.print("비밀번호 (4자리 이상 숫자): ");
        String password = scanner.nextLine();

        if (id.length() >= 4 && password.length() >= 4 && id.matches("\\d{4,}") && password.matches("\\d{4,}")) {
            users.put(id, new User(id, password, name));
            System.out.println("회원가입 성공");
        } else {
            System.out.println("아이디와 비밀번호는 4자리 이상의 숫자여야 합니다.");
        }
        System.out.println("----------------------");
    }

    private static void login() {
        System.out.print("아이디: ");
        String id = scanner.nextLine();
        System.out.print("비밀번호: ");
        String password = scanner.nextLine();

        User user = users.get(id);
        if (user != null && user.password.equals(password)) {
            currentUser = user;
            System.out.println(user.name + "님, 로그인 성공");
        } else {
            System.out.println("로그인 실패");
        }
        System.out.println("----------------------");
    }

    private static void bookContent() {
        if (currentUser == null) {
            System.out.println("먼저 로그인을 해주세요.");
            System.out.println("----------------------");
            return;
        }
        System.out.println("----------------------");
        System.out.println("어떤 콘텐츠를 예매하시겠습니까?");
        System.out.println("1. 콘서트");
        System.out.println("2. 뮤지컬");
        System.out.println("3. 페스티벌");
        System.out.print("선택: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // clear the buffer

        String contentType = "";
        switch (choice) {
            case 1:
                contentType = "콘서트";
                break;
            case 2:
                contentType = "뮤지컬";
                break;
            case 3:
                contentType = "페스티벌";
                break;
            default:
                System.out.println("잘못된 선택입니다.");
                System.out.println("----------------------");
                return;
        }

        List<Content> availableContents = contents.get(contentType);
        if (availableContents != null && !availableContents.isEmpty()) {
        	System.out.println("----------------------");
            System.out.println(contentType + " 목록:");
            for (int i = 0; i < availableContents.size(); i++) {
                System.out.println((i + 1) + ". " + availableContents.get(i).name);
            }
            System.out.print("선택: ");
            int contentChoice = scanner.nextInt();
            scanner.nextLine(); // clear the buffer

            if (contentChoice > 0 && contentChoice <= availableContents.size()) {
                Content selectedContent = availableContents.get(contentChoice - 1);
                System.out.print("예매 하시겠습니까? (Y/N): ");
                String confirmation = scanner.nextLine();

                if (confirmation.equalsIgnoreCase("Y")) {
                    selectSeat(selectedContent);
                } else if (confirmation.equalsIgnoreCase("N")) {
                    System.out.println("예매가 취소되었습니다.");
                } else {
                    System.out.println("잘못된 선택입니다.");
                }
            } else {
                System.out.println("잘못된 선택입니다. 이전 메뉴로 돌아갑니다.");
            }
        } else {
            System.out.println(contentType + "이(가) 없습니다.");
        }
        System.out.println("----------------------");
    }

    private static void selectSeat(Content content) {
        System.out.println("좌석을 선택하세요:");
        System.out.println("1. VIP석");
        System.out.println("2. General석");
        System.out.print("선택: ");
        int seatChoice = scanner.nextInt();
        scanner.nextLine(); // clear the buffer

        String seatType = "";
        switch (seatChoice) {
            case 1:
                seatType = "VIP석";
                break;
            case 2:
                seatType = "General석";
                break;
            default:
                System.out.println("잘못된 선택입니다.");
                System.out.println("----------------------");
                return;
        }

        selectSeats(seatType, content);
    }

    private static void selectSeats(String seatType, Content content) {
        while (true) {
        	System.out.println("----------------------");
            System.out.print("좌석 번호를 선택해주세요 (남은 좌석: " + (TOTAL_SEATS - bookedSeats.size()) + "): ");
            String seatInput = scanner.nextLine();
            String[] seatNumbers = seatInput.split(" ");
            List<Integer> selectedSeats = new ArrayList<>();

            boolean valid = true;
            for (String seat : seatNumbers) {
                try {
                    int seatNumber = Integer.parseInt(seat);
                    if (seatNumber < 1 || seatNumber > TOTAL_SEATS || bookedSeats.contains(seatNumber)) {
                        System.out.println("좌석 번호가 유효하지 않거나 이미 선택된 좌석입니다: " + seatNumber);
                        valid = false;
                        break;
                    }
                    selectedSeats.add(seatNumber);
                } catch (NumberFormatException e) {
                    System.out.println("잘못된 입력입니다: " + seat);
                    valid = false;
                    break;
                }
            }

            if (valid) {
                bookedSeats.addAll(selectedSeats);
                System.out.println("선택된 좌석: " + selectedSeats);
                selectPaymentMethod(content, seatType, selectedSeats);
                break;
            }
        }
    }

    private static void selectPaymentMethod(Content content, String seatType, List<Integer> selectedSeats) {
        while (true) {
        	System.out.println("----------------------");
            System.out.println("결제 정보를 선택하세요:");
            System.out.println("1. Credit Card");
            System.out.println("2. PayPal");
            System.out.println("3. Bank Transfer");
            System.out.print("선택: ");
            int paymentChoice = scanner.nextInt();
            scanner.nextLine(); // clear the buffer

            String paymentMethod = "";
            switch (paymentChoice) {
                case 1:
                    paymentMethod = "Credit Card";
                    break;
                case 2:
                    paymentMethod = "PayPal";
                    break;
                case 3:
                    paymentMethod = "Bank Transfer";
                    break;
                default:
                    System.out.println("잘못된 선택입니다.");
                    System.out.println("----------------------");
                    return;
            }

            System.out.print("결제 정보를 입력하세요 (12자리): ");
            String paymentNumber = scanner.nextLine();

            if (paymentNumber.length() == 12 && paymentNumber.matches("\\d{12}")) {
                processPayment(content, seatType, selectedSeats, paymentMethod, paymentNumber);
                break;
            } else {
                System.out.println("입력 실패, 전 단계로 돌아갑니다.");
                System.out.println("----------------------");
            }
        }
    }

    private static void processPayment(Content content, String seatType, List<Integer> selectedSeats, String paymentMethod, String paymentNumber) {
        currentUser.cart.add(new Booking(content, seatType, selectedSeats, paymentMethod, paymentNumber));
        System.out.println("----------------------");
        System.out.println("결제가 완료되었습니다.");
        System.out.println("예매 정보:");
        System.out.println("콘텐츠: " + content.name);
        System.out.println("좌석 타입: " + seatType);
        System.out.println("선택된 좌석: " + selectedSeats);
        System.out.println("결제 방법: " + paymentMethod);
        System.out.println("결제 정보: " + paymentNumber);
    }

    private static void viewCart() {
        if (currentUser == null) {
            System.out.println("먼저 로그인을 해주세요.");
            System.out.println("----------------------");
            return;
        }

        if (currentUser.cart.isEmpty()) {
        	System.out.println("----------------------");
            System.out.println("장바구니가 비었습니다.");
        } else {
        	System.out.println("----------------------");
            System.out.println("장바구니 내용:");
            for (int i = 0; i < currentUser.cart.size(); i++) {
                Booking booking = currentUser.cart.get(i);
                System.out.println((i + 1) + ". " + booking);
            }
        }
        System.out.println("----------------------");
    }

    private static void loadContents() {
        contents.put("콘서트", Arrays.asList(
            new Content("콘서트", "2024 aespa LIVE TOUR-SYNK：PARALLEL LINE-"),
            new Content("콘서트", "백예린 -Squre(2024)")
        ));

        contents.put("뮤지컬", Arrays.asList(
            new Content("뮤지컬", "벤자민 버튼"),
            new Content("뮤지컬", "프랑켄슈타인")
        ));
        contents.put("페스티벌", Arrays.asList(
            new Content("페스티벌", "제16회 서울재즈페스티벌 2024")
        ));
    }
}