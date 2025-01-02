Ollama is an open source llm (large language model)

The language model that is used in this example is llama version 3.1

This is a sample test using ollama with spring ai to perform chat related or other tasks

Can be executed via browser localhost / postman / any other methods that can be used to call api

Tasks performed in sample project
1. The default path requests the AI Model to write a simple landing page in html - prompts to generate response based on questions asked
2. The second path which is localhost:8080/cities?message="Insert message here" which requests the AI model to look up on the current weather based on the city provided in the message
3. Upload document into embedding format and requests AI to generate response based on information in document submitted - currently use document located in classpath

Additional Notes
1. The API Url for weather is http://api.weatherapi.com/v1 and the api key is generated based on account generated. If you would like to test it please create an account in the url provided. Thank you. 
