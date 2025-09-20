import requests

# 서버 주소 (Flask 실행 중인 주소)
url = "http://localhost:5000/ocr-caption"  # 또는 /picture-caption

# 보낼 이미지 경로
image_path = "C:\\Users\\cat\\Desktop\\Ganada-Backend-AI-Server\\text.jpg"  # 테스트할 파일명 입력

# 파일 전송
files = {'file': open(image_path, 'rb')}
response = requests.post(url, files=files)

# 결과 출력
print(response.json())
