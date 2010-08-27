import cgi
import os

from google.appengine.api import users
from google.appengine.ext import db
from google.appengine.ext import webapp
from google.appengine.ext.webapp.util import run_wsgi_app
from google.appengine.ext.webapp import template

import gdata.service
import gdata.photos.service
import gdata.media
import gdata.geo
import gdata.alt.appengine

class MainPage(webapp.RequestHandler):
    def get(self):
        
        self.response.headers['Content-Type'] = 'text/html'
        
        #login
        url=None
        url_linktext=None
        user = users.get_current_user()
        if user==None:
            url = users.create_login_url(self.request.uri)
            url_linktext = 'Login'
            self.redirect(url)
            return
        else:
            url = users.create_logout_url(self.request.uri)
            url_linktext = 'Logout'
            
            
        #get album data
        albumlist=[]
        
            
        #get photo data
        #photolist=[]
        #photos=None
        client = gdata.photos.service.PhotosService()
        gdata.alt.appengine.run_on_appengine(client)
        
        albums = client.GetUserFeed(user=user)
        for album in albums.entry:
            albumlist.append(album)
            #photos = client.GetFeed('/data/feed/base/user/%s/albumid/%s?kind=photo' % (user, album.gphoto_id.text))
            #for photo in photos.entry:
            #    photolist.append(photo.media.content[0].url)

        template_values={
                'user':user,
                'albumlist': albumlist,
                'url': url,
                'url_linktext': url_linktext,
        }

        path = os.path.join(os.path.dirname(__file__), 'album.html')
        self.response.out.write(template.render(path, template_values))

application = webapp.WSGIApplication([('/', MainPage)],debug=True)

def main():
  run_wsgi_app(application)

if __name__ == "__main__":
  main()