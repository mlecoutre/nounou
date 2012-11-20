var zombie = require('zombie'),
vows = require('vows'),
assert = require('assert');

var baseUrl = 'http://www.google.ca/';

vows.describe('Zombie Tests on Google.ca').addBatch({
'Navigate to Google.ca' : {
    topic: function () {
	browser = new zombie.Browser({ debug: true });
	browser.runScripts = false;
	browser.on('error',function (err){console.log(err.stack)});
	browser.on('done',function (done){console.log(done.document.cookie)});
	browser.visit(baseUrl, this.callback);
 	},
    'Should be on Google homepage' : function (browser) {
	assert.equal(browser.location, baseUrl);
	},
    'Title should be correct' : function (browser) {
	assert.equal(browser.text("title"), "Google");
	},
    'Browser is not redirected' : function (browser) {
	var redirect = browser.redirected;
	assert.equal(redirect,false);
	},
    'There is a button with name "btnG"' : function(browser) {
	assert.ok(browser.button("btnG"));
	},
    'and Search for "Test"' : {
	topic: function (browser) {
		browser.fill("q","Test");
		browser.pressButton("btnG",this.callback);
		},
	'Title is correct' : function (browser) {
		assert.equal(browser.text("title"), "Test - Google Search");
		}
	}
  },
}).export(module);