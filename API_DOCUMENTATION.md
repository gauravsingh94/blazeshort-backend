# Overall Dashboard API Endpoint Documentation

## Endpoint: Get Overall Dashboard Analytics

### HTTP Request
```
GET /api/dashboard/overall
```

### Authentication
- Required: Yes (Bearer Token in Authorization header)
- Type: JWT Token

### Request Headers
```
Authorization: Bearer <jwt_token>
Content-Type: application/json
```

### Path Parameters
None

### Query Parameters
None

### Request Body
No request body required

### Response Status Codes
| Code | Description |
|------|-------------|
| 200 | Success - Returns overall dashboard analytics |
| 401 | Unauthorized - Invalid or missing JWT token |
| 403 | Forbidden - User does not have permission |
| 500 | Internal Server Error |

### Success Response (200)

```json
{
  "totalUrls": 25,
  "totalClicks": 5420,
  "uniqueClicks": 1250,
  "activeUrls": 20,
  "expiredUrls": 3,
  "disabledUrls": 2,
  "clicksPerDay": [
    {
      "date": "2024-04-01",
      "count": 150
    },
    {
      "date": "2024-04-02",
      "count": 200
    },
    {
      "date": "2024-04-03",
      "count": 175
    }
  ],
  "topIps": [
    {
      "ip": "192.168.1.1",
      "count": 250
    },
    {
      "ip": "10.0.0.1",
      "count": 200
    },
    {
      "ip": "172.16.0.1",
      "count": 180
    }
  ],
  "topUserAgents": [
    {
      "userAgent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
      "count": 1500
    },
    {
      "userAgent": "Mozilla/5.0 (iPhone; CPU iPhone OS 14_7_1 like Mac OS X)",
      "count": 1200
    },
    {
      "userAgent": "Mozilla/5.0 (X11; Linux x86_64)",
      "count": 800
    }
  ],
  "topPerformingUrls": [
    {
      "shortCode": "abc123",
      "originalUrl": "https://example.com/product/summer-sale",
      "totalClicks": 500,
      "uniqueClicks": 250
    },
    {
      "shortCode": "def456",
      "originalUrl": "https://example.com/blog/article-title",
      "totalClicks": 450,
      "uniqueClicks": 200
    },
    {
      "shortCode": "ghi789",
      "originalUrl": "https://example.com/promo/special-offer",
      "totalClicks": 400,
      "uniqueClicks": 180
    }
  ],
  "recentClicks": [
    {
      "ip": "192.168.1.100",
      "userAgent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64)",
      "clickedAt": "2024-04-05T15:30:45"
    },
    {
      "ip": "10.0.0.50",
      "userAgent": "Mozilla/5.0 (iPhone; CPU iPhone OS 14_7_1)",
      "clickedAt": "2024-04-05T15:29:22"
    },
    {
      "ip": "172.16.0.25",
      "userAgent": "Mozilla/5.0 (X11; Linux x86_64)",
      "clickedAt": "2024-04-05T15:28:10"
    }
  ]
}
```

### Response Field Descriptions

| Field | Type | Description |
|-------|------|-------------|
| totalUrls | Long | Total number of short URLs created by the user |
| totalClicks | Long | Sum of all clicks across all URLs |
| uniqueClicks | Long | Total count of unique IP addresses that clicked any URL |
| activeUrls | Long | Number of URLs with ACTIVE status |
| expiredUrls | Long | Number of URLs with EXPIRED status |
| disabledUrls | Long | Number of URLs with DISABLED status |
| clicksPerDay | Array | Daily breakdown of clicks (sorted by date) |
| clicksPerDay[].date | String | Date in YYYY-MM-DD format |
| clicksPerDay[].count | Long | Number of clicks on that date |
| topIps | Array | Top 10 IP addresses by click count |
| topIps[].ip | String | IP address |
| topIps[].count | Long | Number of clicks from this IP |
| topUserAgents | Array | Top 10 user agents by click count |
| topUserAgents[].userAgent | String | User agent string |
| topUserAgents[].count | Long | Number of clicks with this user agent |
| topPerformingUrls | Array | Top 10 most clicked short URLs |
| topPerformingUrls[].shortCode | String | The short URL code |
| topPerformingUrls[].originalUrl | String | The original long URL |
| topPerformingUrls[].totalClicks | Long | Total clicks for this URL |
| topPerformingUrls[].uniqueClicks | Long | Unique visitors for this URL |
| recentClicks | Array | Last 10 clicks across all URLs |
| recentClicks[].ip | String | IP address of the click |
| recentClicks[].userAgent | String | User agent of the click |
| recentClicks[].clickedAt | DateTime | Timestamp of the click |

### cURL Examples

#### Basic Request
```bash
curl -X GET "http://localhost:8080/api/dashboard/overall" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -H "Content-Type: application/json"
```

#### Using jq for formatted output
```bash
curl -s -X GET "http://localhost:8080/api/dashboard/overall" \
  -H "Authorization: Bearer <token>" | jq '.'
```

#### Save response to file
```bash
curl -X GET "http://localhost:8080/api/dashboard/overall" \
  -H "Authorization: Bearer <token>" \
  -o dashboard_data.json
```

### JavaScript/Fetch Example

```javascript
const token = 'your_jwt_token_here';

async function getOverallDashboard() {
  try {
    const response = await fetch('/api/dashboard/overall', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const data = await response.json();
    console.log('Dashboard Analytics:', data);
    
    // Display summary
    console.log(`Total URLs: ${data.totalUrls}`);
    console.log(`Total Clicks: ${data.totalClicks}`);
    console.log(`Unique Visitors: ${data.uniqueClicks}`);
    console.log(`Active URLs: ${data.activeUrls}`);
    
    return data;
  } catch (error) {
    console.error('Error fetching dashboard:', error);
  }
}

// Call the function
getOverallDashboard();
```

### Python/Requests Example

```python
import requests
import json

token = 'your_jwt_token_here'
headers = {
    'Authorization': f'Bearer {token}',
    'Content-Type': 'application/json'
}

try:
    response = requests.get(
        'http://localhost:8080/api/dashboard/overall',
        headers=headers
    )
    response.raise_for_status()
    
    data = response.json()
    print("Overall Dashboard Analytics")
    print(f"Total URLs: {data['totalUrls']}")
    print(f"Total Clicks: {data['totalClicks']}")
    print(f"Unique Visitors: {data['uniqueClicks']}")
    print(f"Active URLs: {data['activeUrls']}")
    print(f"Expired URLs: {data['expiredUrls']}")
    print(f"Disabled URLs: {data['disabledUrls']}")
    
    # Print top performing URLs
    print("\nTop 5 Performing URLs:")
    for i, url in enumerate(data['topPerformingUrls'][:5], 1):
        print(f"{i}. {url['shortCode']} - {url['totalClicks']} clicks")
        
except requests.exceptions.RequestException as error:
    print(f"Error: {error}")
```

### Comparison with Individual URL Analytics

#### Individual URL Analytics
- **Endpoint**: `GET /api/dashboard/analytics/{code}`
- **Returns**: Analytics for a single shortened URL
- **Use case**: Detailed analysis of one specific URL

#### Overall Dashboard Analytics
- **Endpoint**: `GET /api/dashboard/overall`
- **Returns**: Aggregated analytics for all user's URLs
- **Use case**: Executive summary, SaaS dashboard view

### Rate Limiting

- This endpoint follows the same rate limiting rules as other dashboard endpoints
- See RateLimitConfig for specific limits

### Caching Recommendations

For performance optimization, consider caching this endpoint's response at the frontend:
- Cache duration: 5-10 minutes (depending on your needs)
- Invalidate cache when: new URL created, URL disabled/enabled, data is stale

### Performance Notes

- Queries are optimized with proper JPA joins
- Results are limited (top 10 for most fields, last 10 for recent clicks)
- Suitable for real-time dashboard updates
- No N+1 query problems with proper join strategies

### Error Handling

The endpoint may return these error responses:

#### 401 Unauthorized
```json
{
  "error": "Unauthorized",
  "message": "Invalid or expired token"
}
```

#### 403 Forbidden
```json
{
  "error": "Forbidden",
  "message": "You do not have permission to access this resource"
}
```

#### 500 Internal Server Error
```json
{
  "error": "Internal Server Error",
  "message": "An unexpected error occurred"
}
```

### Changelog

**v1.0**
- Initial release of overall dashboard endpoint
- Includes URL status breakdown
- Includes top performers and recent clicks

