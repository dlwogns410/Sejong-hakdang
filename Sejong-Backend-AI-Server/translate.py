import os
import configparser as parser
import openai

# config.ini에서 GPT API 키 로드
properties = parser.ConfigParser()
properties.read('./config.ini', encoding='utf-8')
openai.api_key = properties['GPT']['api_key']


def translate_en_to_ko(text):
    """
    영어 문장을 자연스러운 한국어로 번역하는 함수
    (사물 인식 결과에 사용)
    """
    prompt = f"Translate the following sentence into natural Korean:\n\n{text}"

    try:
        response = openai.ChatCompletion.create(
            model="gpt-3.5-turbo",
            messages=[
                {"role": "system", "content": "You are a helpful AI translator that translates English to Korean accurately and naturally."},
                {"role": "user", "content": prompt}
            ],
            temperature=0.7,
            max_tokens=100
        )
        translated_text = response['choices'][0]['message']['content'].strip()
        return translated_text

    except Exception as e:
        print("GPT Translation Error:", e)
        return "번역 실패"


def generate_korean_example(word):
    """
    한글 단어를 활용한 짧고 쉬운 예문 생성
    """
    prompt = f"'{word}'라는 단어를 사용해서, 초등학생도 이해할 수 있는 자연스러운 한국어 문장 하나만 만들어줘."

    try:
        response = openai.ChatCompletion.create(
            model="gpt-3.5-turbo",  # 또는 "gpt-4"
            messages=[
                {"role": "system", "content": "당신은 초등학생을 위한 쉬운 문장을 만드는 AI입니다."},
                {"role": "user", "content": prompt}
            ],
            temperature=0.5,
            max_tokens=60
        )
        return response['choices'][0]['message']['content'].strip()
    except Exception as e:
        print("예문 생성 오류:", e)
        return "예문 없음"
