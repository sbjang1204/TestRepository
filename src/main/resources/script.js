const complimentButton = document.getElementById("complimentButton");
const resultText = document.getElementById("resultText");
const cutenessRange = document.getElementById("cutenessRange");
const meterValue = document.getElementById("meterValue");

const compliments = [
    "측정 결과: 코의 원형 완성도가 높아 귀여움 지수가 99.2점으로 상승했습니다.",
    "분석 결과: 짧은 다리의 리듬감이 매우 우수하여 힐링 효과가 강합니다.",
    "관찰 결과: 통통한 볼이 전체 인상을 부드럽게 만들어 연구진이 조용히 감탄했습니다.",
    "최종 결론: 작은 귀와 둥근 실루엣의 조화가 상당히 모범적입니다."
];

if (complimentButton && resultText) {
    complimentButton.addEventListener("click", () => {
        const randomIndex = Math.floor(Math.random() * compliments.length);
        resultText.textContent = compliments[randomIndex];
    });
}

if (cutenessRange && meterValue) {
    const syncMeter = () => {
        meterValue.textContent = cutenessRange.value + "%";
    };

    cutenessRange.addEventListener("input", syncMeter);
    syncMeter();
}
