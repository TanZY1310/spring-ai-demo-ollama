// Base URL for the API
const URL = "http://localhost:8080";

const btnEl = document.querySelector('#generateButton');
const generateButton = document.getElementById("generateButton");
const buttonSpinner = document.getElementById("buttonSpinner");
const buttonText = document.getElementById("buttonText");

async function generateResponse() {

    //Reset output
    document.getElementById("resultsId").textContent = "";
    const promptValue = document.getElementById("prompt").value;
    if(!promptValue) {
        alert("Please enter a prompt.");
        return;
    }

    //Add loading spinner to simulate generating response
    generateButton.disabled = true;
    buttonSpinner.style.display = "inline-block";
    buttonText.style.display = "none";

    var result = "Failed to Fetch Response";
    try {
        //Call API to generate response
        const response = await fetch(`${URL}/prompt?prompt=${promptValue}`);
        if (!response.ok) {
            throw new Error("Error generating response");
        }

        result = await response.text();
        //Update response to output section
        document.getElementById("resultsId").textContent = result;
    } catch (error) {
        console.error(error);
        document.getElementById("resultsId").textContent = result;
    }
    document.getElementById("resultsId").textContent = result;

    generateButton.disabled = false;
    buttonSpinner.style.display = "none";
    buttonText.style.display = "inline";
}