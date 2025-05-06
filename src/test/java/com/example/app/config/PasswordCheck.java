// パスワード確認用（開発時のみ使用）
// ログイン不具合などの調査に使うツールクラス
//package com.example.app.config;
//
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//public class PasswordCheck {
//    public static void main(String[] args) {
//        String encodedPassword = "$2a$10$5nZFBEpH6jbx0o7.0N1VgOSzDeORitrXRURih/J5k5kPasq.4I1zq";
//
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//
//        // チェックしたい平文パスワードをここに入れてみる
//        String plainPassword = "test";
//
//        boolean matches = encoder.matches(plainPassword, encodedPassword);
//
//        System.out.println("一致する？ → " + matches);
//        
//    }
//    
//}
