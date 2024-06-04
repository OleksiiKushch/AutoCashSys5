function changeLocale() {
    const url = new URL(window.location.href);

    url.searchParams.set('locale', document.querySelector('#selectLocale').value);

    window.location.href = url.href;
}