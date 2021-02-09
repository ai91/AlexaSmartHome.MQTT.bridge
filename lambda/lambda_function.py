"""
--------------------  REFERENCE MATERIALS  --------------------
Smart Home Skill API Reference:
https://developer.amazon.com/public/solutions/alexa/alexa-skills-kit/docs/smart-home-skill-api-reference
Python reference for url/http handling
https://docs.python.org/2/howto/urllib2.html
---------------------------------------------------------------
"""
import json
import urllib.request
import logging

logger = logging.getLogger()
logger.setLevel(logging.INFO)

# ============================================================================
#   Default Handler for all function requests
# ============================================================================
def lambda_handler(event, context):

    logger.info('**  Event Request  ** = {}'.format(event))

    # Set the request URL to host:port.
    url = 'https://example.com/alexamqttbridge/api/events'

    # Encode the request event data.
    data = json.dumps(event)
    data = data.encode('utf-8')
	
    # Perform the request with the event data.
    req = urllib.request.Request(url, data)
    req.add_header('Content-Type' , 'application/json')
    
    # add Bearer token (use from event.directive.endpoint.scope.token, when event.directive.endpoint.scope.type == 'BearerToken')
    if 'directive' in event and 'endpoint' in event['directive'] and 'scope' in event['directive']['endpoint'] and 'type' in event['directive']['endpoint']['scope'] and event['directive']['endpoint']['scope']['type'] == 'BearerToken':
        token = event['directive']['endpoint']['scope']['token']
        req.add_header('Authorization' , 'Bearer {}'.format(token))
    elif 'directive' in event and 'payload' in event['directive'] and 'scope' in event['directive']['payload'] and 'type' in event['directive']['payload']['scope'] and event['directive']['payload']['scope']['type'] == 'BearerToken':
        token = event['directive']['payload']['scope']['token']
        req.add_header('Authorization' , 'Bearer {}'.format(token))
    else:
        logger.error('**  No Bearer token found in request  = {}'.format(event))

    # Create the response object.
    response = urllib.request.urlopen(req)

    # Get the complete response page.
    response_str = response.read().decode('utf-8')
    logger.info('**  REST Response  ** = {}'.format(response_str))

    # Load raw page data into JSON object.
    parsed_json = json.loads(response_str)
    #logger.debug('**  Parsed JSON  ** = {}'.format(parsed_json))


    # Return value as a JSON object.
    return parsed_json
