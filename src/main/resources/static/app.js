const analyzeBtn = document.getElementById('analyzeBtn');
const moodText = document.getElementById('moodText');
const emotionsCard = document.getElementById('emotionsCard');
const emotionsContainer = document.getElementById('emotionsContainer');
const mediaCard = document.getElementById('mediaCard');
const mediaQuestion = document.getElementById('mediaQuestion');
const resultsCard = document.getElementById('resultsCard');
const resultsList = document.getElementById('resultsList');
const rawOutput = document.getElementById('rawOutput');
const status = document.getElementById('status');

let selectedEmotion = null;

function setStatus(msg, cls = 'text-muted') {
    status.className = cls;
    status.textContent = msg;
}

analyzeBtn.addEventListener('click', async () => {
    const text = moodText.value.trim();
    if (!text) {
        setStatus('Please enter your mood description.', 'text-danger');
        return;
    }
    setStatus('Analyzing...', 'text-muted');
    try {
        const resp = await fetch('/api/analyze', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({moodText: text})
        });
        if (!resp.ok) throw new Error('Network response not ok');
        const data = await resp.json();
        showEmotions(data.emotions);
        setStatus('Select an emotion.');
    } catch (e) {
        console.error(e);
        setStatus('Error analyzing mood.', 'text-danger');
    }
});

function showEmotions(emotions) {
    emotionsContainer.innerHTML = '';
    if (!emotions || emotions.length === 0) {
        setStatus('No emotions detected.', 'text-warning');
        return;
    }
    emotionsCard.classList.remove('d-none');
    mediaCard.classList.add('d-none');
    resultsCard.classList.add('d-none');
    selectedEmotion = null;
    emotions.forEach(em => {
        const btn = document.createElement('button');
        btn.className = 'btn btn-outline-secondary emotion-btn';
        btn.textContent = em;
        btn.dataset.emotion = em;
        btn.addEventListener('click', onEmotionClick);
        emotionsContainer.appendChild(btn);
    });
}

function onEmotionClick(e) {
    const em = e.currentTarget.dataset.emotion;
    selectedEmotion = em;
    Array.from(emotionsContainer.children).forEach(b => {
        b.classList.remove('btn-primary');
        b.classList.add('btn-outline-secondary');
    });
    e.currentTarget.classList.remove('btn-outline-secondary');
    e.currentTarget.classList.add('btn-primary');
    mediaQuestion.textContent = `Would you like songs or albums related to "${em}"?`;
    mediaCard.classList.remove('d-none');
    resultsCard.classList.add('d-none');
    rawOutput.classList.add('d-none');
    setStatus('Select a media type to get recommendations.', 'text-muted');
}

document.getElementById('songsBtn').addEventListener('click', () => chooseMedia('SONGS'));
document.getElementById('albumsBtn').addEventListener('click', () => chooseMedia('ALBUMS'));

async function chooseMedia(type) {
    if (!selectedEmotion) {
        setStatus('Select an emotion first.', 'text-danger');
        return;
    }
    setStatus('Fetching recommendations...', 'text-muted');
    try {
        const resp = await fetch('/api/recommend', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                moodText: moodText.value.trim(),
                emotion: selectedEmotion,
                mediaType: type
            })
        });
        if (!resp.ok) throw new Error('Network response not ok');
        const data = await resp.json();
        showResults(data.items, data.raw);
        setStatus('');
    } catch (e) {
        console.error(e);
        setStatus('Error getting recommendations.', 'text-danger');
    }
}

function showResults(items, raw) {
    resultsList.innerHTML = '';
    if (items && items.length > 0) {
        items.forEach(it => {
            const li = document.createElement('li');
            li.className = 'list-group-item';
            li.textContent = it.title + " by " + it.artist + " - " + it.description;
            resultsList.appendChild(li);
        });
    } else {
        const li = document.createElement('li');
        li.className = 'list-group-item';
        li.textContent = 'No recommendations.';
        resultsList.appendChild(li);
    }
    resultsCard.classList.remove('d-none');
    rawOutput.textContent = raw || '';
    rawOutput.classList.remove('d-none');
}
