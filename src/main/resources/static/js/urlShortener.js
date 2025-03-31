document.addEventListener("DOMContentLoaded", function () {
  const urlForm = document.getElementById("url-form");
  const originalUrlInput = document.getElementById("original-url");
  const resultContainer = document.getElementById("result-container");
  const errorMessage = document.getElementById("error-message");
  const originalUrlDisplay = document.getElementById("original-url-display");
  const shortUrlDisplay = document.getElementById("short-url-display");
  const copyBtn = document.getElementById("copy-btn");
  const copySuccess = document.getElementById("copy-success");

  urlForm.addEventListener("submit", async function (e) {
    e.preventDefault();

    const originalUrl = originalUrlInput.value.trim();

    resultContainer.style.display = "none";
    errorMessage.style.display = "none";
    copySuccess.style.display = "none";

    if (!originalUrl) {
      showError("Please enter a URL");
      return;
    }

    try {
      const response = await fetch("/api/url", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ url: originalUrl }),
      });

      const data = await response.json();

      if (response.ok) {
        const shortUrl = `${window.location.origin}/${data.shortUrl}`;

        originalUrlDisplay.textContent = data.originalUrl;
        shortUrlDisplay.textContent = shortUrl;
        resultContainer.style.display = "block";
      } else {
        showError(data.error || "An error occurred while shortening the URL.");
      }
    } catch (error) {
      showError("An error occurred while shortening the URL.");
      console.error(error);
    }
  });

  // Copy to clipboard functionality
  copyBtn.addEventListener("click", function () {
    const shortUrl = shortUrlDisplay.textContent;

    navigator.clipboard
      .writeText(shortUrl)
      .then(() => {
        copySuccess.style.display = "inline";
        setTimeout(() => {
          copySuccess.style.display = "none";
        }, 2000);
      })
      .catch((err) => {
        console.error("Could not copy text: ", err);
      });
  });

  // Helper function to show error message
  function showError(message) {
    errorMessage.textContent = message;
    errorMessage.style.display = "block";
  }
});
