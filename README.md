# 🌿 마음이 서버 (maeum-server)

## 📖 소개
마음이 앱의 Spring Boot 백엔드 서버입니다.

## 🛠 기술 스택
- ☕ Java 24
- 🍃 Spring Boot 3.5.14
- 🐬 MySQL 8.0
- 🔐 JWT (Access Token + Refresh Token)
- 🔔 Firebase FCM
- 🤖 OpenAI API
- 🟡 카카오 로그인 REST API

## ✨ 주요 기능
- 🟡 카카오 소셜 로그인
- 🔐 JWT 기반 인증 (자동 로그인 유지)
- 💚 감정 수치 기록 및 조회
- 🤖 AI 감정 분석 (OpenAI API)
- 📝 일기 작성/조회/수정
- 📊 주간/월간 감정 통계
- 🔔 FCM 푸시 알림

## 🔗 API 엔드포인트
- 🔑 `/auth` - 인증 (카카오 로그인, 토큰 재발급, 로그아웃)
- 👤 `/users` - 유저 정보 조회/수정/탈퇴
- 📝 `/diaries` - 일기 CRUD
- 💚 `/emotions` - 감정 기록 저장/조회
- 🤖 `/ai-analysis` - AI 감정 분석
- 📊 `/statistics` - 주간/월간 통계
- 🔔 `/notifications` - 알림 설정

## 🔗 연동 프로젝트
- 📱 Flutter 앱: [maeum](https://github.com/Hello11234567/Maeum)
